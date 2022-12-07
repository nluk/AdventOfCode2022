import java.util.Objects

class Day7 {

    val directoryLookupSet = mutableSetOf<DirectoryNode>()

    sealed class TreeNode

    object NullNode : TreeNode()

    data class FileNode(val size : Int) : TreeNode()

    class DirectoryNode(
        val name : String,
        val parent : TreeNode,
        var totalSize : Int = 0,
        val children : MutableMap<String, TreeNode> = mutableMapOf()
    ) : TreeNode(){

        fun addFile(fileLine : String){
            val (fileSize, filename) = fileLine.split(" ")
            val node = FileNode(fileSize.toInt())
            children[filename] = node
            incrementSize(node.size)
        }

        fun incrementSize(size : Int){
            totalSize += size
            if(parent !== NullNode){
                (parent as DirectoryNode).incrementSize(size)
            }
        }

        override fun hashCode(): Int {
            return Objects.hash(name, parent)
        }
    }

    val root = DirectoryNode("/", NullNode)
    var currentDir : DirectoryNode = root

    fun addDir(dirLine : String){
        val (_, dirname) = dirLine.split(" ")
        val node = DirectoryNode(dirname, currentDir)
        currentDir.children[dirname] = node
        directoryLookupSet.add(node)
    }

    fun parseTree(input : Sequence<String>){
        for (line in input){
            if (line.startsWith('$')){
                val command = line.split(" ")
                if(command[1] == "cd"){
                    currentDir = when(command[2]){
                        ".." -> if(currentDir === root) root else currentDir.parent as DirectoryNode
                        "/" -> root
                        else -> currentDir.children[command[2]]!! as DirectoryNode
                    }
                }
            }
            else {
                if(line.startsWith("dir")){
                    addDir(line)
                }
                else {
                    currentDir.addFile(line)
                }
            }
        }
    }

    fun part1(){
        directoryLookupSet
            .asSequence()
            .filter {
                it.name != "/"  && it.totalSize <= 100000
            }
            .map {
                it.totalSize
            }.sum()
            .also(::println)
    }

    fun part2() {
        val required = 30000000
        val total = 70000000
        val available = total - root.totalSize
        val requiredToRemove = required - available
        directoryLookupSet
            .asSequence()
            .filter { it.totalSize >= requiredToRemove }
            .sortedBy { it.totalSize - requiredToRemove }
            .first()
            .also {
                println(it.totalSize)
            }
    }

}

fun main() = solve(day = 7) { input ->
    with(Day7()){
        parseTree(input)
        part1()
        part2()
    }
}