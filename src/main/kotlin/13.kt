import java.util.Stack

fun StringBuilder.dropToInt() : Int {
    val int = this.toString().toInt()
    clear()
    return int
}

class Day13 {

    sealed class Packet : Comparable<Packet>{
        override fun compareTo(other : Packet) : Int{
            if(this is SinglePacket && other is SinglePacket){
                return this.packet.compareTo(other.packet)
            }
            if(this is CompoundPacket && other is CompoundPacket){
                var result: Int
                val left = this.packets.iterator()
                val right = other.packets.iterator()
                while (left.hasNext() && right.hasNext()){
                    val l = left.next()
                    val r = right.next()
                    result = l.compareTo(r)
                    if(result != 0){
                        return result
                    }
                }
                if(left.hasNext() && !right.hasNext()){
                    return 1
                }
                else if(right.hasNext() && !left.hasNext()){
                    return -1
                }
                return 0
            }
            if(this is CompoundPacket){
                return compareTo(CompoundPacket(mutableListOf(other as SinglePacket)))
            }
            return CompoundPacket(mutableListOf(this as SinglePacket)).compareTo(other)
        }
    }

    data class SinglePacket(val packet : Int) : Packet(){
        override fun toString(): String {
            return packet.toString()
        }
    }

    data class CompoundPacket(val packets: MutableList<Packet> = mutableListOf()) : Packet() {
        override fun toString(): String {
            return packets.toString()
        }
    }

    fun parsePacket(packetStr : String) : CompoundPacket{
        val packetStack = Stack<CompoundPacket>()
        packetStack.push(CompoundPacket())
        val intBuffer = StringBuilder(100)
        for(c in packetStr.subSequence(1, packetStr.length - 1)){
            if(c == '['){
                val nested = CompoundPacket()
                packetStack.peek().packets.add(nested)
                packetStack.push(nested)
            }
            else if(c == ']'){
                if(intBuffer.isNotEmpty()){
                    packetStack.peek().packets.add(SinglePacket(intBuffer.dropToInt()))
                }
                packetStack.pop()
            }
            else if(c == ',' && intBuffer.isNotEmpty()){
                packetStack.peek().packets.add(SinglePacket(intBuffer.dropToInt()))
            }
            else if(c.isDigit()){
                intBuffer.append(c)
            }
        }
        return packetStack.pop()
    }

    fun part1() = solve(day = 13){ input ->
       input.chunked(3)
           .withIndex()
           .map { (index, packetStrings) ->
               val packetA = parsePacket(packetStrings[0])
               val packetB = parsePacket(packetStrings[1])
               index to (packetA <= packetB)
           }.filter {(_, inOrder) ->
               inOrder
           }.sumOf {(index, _) ->
               index + 1
           }
           .also(::println)
    }

    fun part2() = solve(day = 13){ input ->
        val filePackets = input.filter {
            it.isNotBlank()
        }.map {
            parsePacket(it)
        }
        val controlPackets = listOf(
            parsePacket("[[2]]"),
            parsePacket("[[6]]")
        )
        sequence{
            yieldAll(filePackets)
            yieldAll(controlPackets)
        }.sorted()
            .withIndex()
            .filter { (_, packet) ->
                packet == controlPackets.first() || packet == controlPackets.last()
            }.map { (index, _) -> index + 1 }
            .reduce { acc, item ->  acc * item}
            .also(::println)
    }

}

fun main() = with(Day13()){
    part1()
    part2()
}