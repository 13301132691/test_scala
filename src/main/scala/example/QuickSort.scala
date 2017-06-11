package example

/**
  * Created by I330700 on 2017/6/11.
  */
object QuickSort extends App{
  def quickSort(list:List[Int]):List[Int] = {
    list match{
      case Nil => Nil
      case head :: tail =>{
        val (left,right) = tail.partition(_ < head)
        quickSort(left):::head::quickSort(right)
      }
    }
  }
  var list = List(1,4,6,7,9,4,3,5,7)
  print(quickSort(list))
}
