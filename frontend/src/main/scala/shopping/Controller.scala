package shopping

import com.raquo.laminar.api.L.Var
import org.scalajs.dom.Event
import org.scalajs.dom.IDBCreateObjectStoreOptions
import org.scalajs.dom.IDBDatabase
import org.scalajs.dom.IDBEvent
import org.scalajs.dom.IDBTransactionMode
import org.scalajs.dom.window
import shopping.models.Category
import shopping.models.SelectableItem
import shopping.models.ViewModel
import shopping.models.ViewModelState
import shopping.models.ViewModelState.ItemByCategoryView

import java.util.UUID
import java.util.UUID.randomUUID
import scala.scalajs.js
import scala.util.Random

// Various actions for view model
class Controller(dynModel: Var[ViewModel]) {

  // Fetch response processing:
  def onCategoriesFetch(loadedCategories: List[Category]): Unit = {
    dynModel.update(vm => vm.copy(categories = loadedCategories))
  }

  def onItemsFetch(loadedItems: Map[String, List[SelectableItem]]): Unit = {
    dynModel.update(vm => vm.copy(items = loadedItems))
  }

  // Main View
  def onViewButtonPressed(state: ViewModelState) = {
    dynModel.update(vm => vm.copy(state = state))
  }

  // Categories View
  def onCategorySelected(category: Category) = {
    dynModel.update(vm =>
      vm.copy(state = ItemByCategoryView, selectedCategory = Some(category))
    )
  }

  // ItemSelection view
  def onSelectItem(id: String): Unit = {
    dynModel.update(vm =>
      vm.selectedCategory match {
        case Some(category) =>
          vm.items.get(category.cid) match {
            case Some(items) =>
              items.find(_.item.id == id) match {
                case Some(item) =>
                  val itemUpdate = item.copy(selected = !item.selected)
                  val updated = vm.items + (category.cid -> (items
                    .filterNot(e => e.item.id == id) :+ itemUpdate))
                  val updatedBasket = {
                    if (!itemUpdate.selected) {
                      vm.basket.filter(_.item.id == itemUpdate.item.id)
                    } else {
                      vm.basket :+ itemUpdate
                    }
                  }
                  vm.copy(items = updated, basket = updatedBasket)
                case None =>
                  println(s"onSelectItem::Unable to locate item with id: $id")
                  vm
              }
            case None =>
              vm
          }
        case None =>
          println(s"onSelectItem::Unable to locate item with id2: $id")
          vm
      }
    )
  }

  // BasketView
  def onUnSelectedInBasket(id: String): Unit = {
    dynModel.update(vm =>
      vm.basket.find(_.item.id == id) match {
        case Some(selectable) =>
          val itemUpdate = selectable.copy(selected = false)
          // Fina categoryId
          val cid = vm.items
            .find(p => p._2.find(e => e.item.id == id).isDefined)
            .map(_._1)
            .getOrElse("")

          val basketItem = vm.basket.filterNot(e => e.item.id == id)

          vm.categories
            .find(c => c.cid == cid)
            .map(category =>
              val updated = vm.items + (category.cid -> (vm.items
                .get(category.cid)
                .get
                .filterNot(e => e.item.id == id) :+ itemUpdate))
              vm.copy(items = updated, basket = basketItem)
            )
            .getOrElse(vm)
        case None =>
          vm
      }
    )
  }

  def fromJsDate(date: js.Date): String = {
    val day = date.getDate().toInt
    val dayOfWeek = date.getDay().toInt
    val month = date.getMonth().toInt + 1
    val year = date.getFullYear().toInt

    s"$day-$month-$year"
  }

  // https://gist.github.com/JamesMessinger/a0d6389a5d0e3a24814b
  def onBtnArchive() = {
    // Archive items:

    val indexDb = window.indexedDB.get
    val open = indexDb.open("ShoppingDb", 1)

    // TODO: need to have a unique identified for each record/batch saved
    // Upgrade
    open.onupgradeneeded = (e: IDBEvent[IDBDatabase]) => {
      val db = e.target.result
      val opts = new IDBCreateObjectStoreOptions {
        override val keyPath = "id" // works like RDB index
      }
      val store = db.createObjectStore("ShoppingDb", opts)
      store.createIndex(
        "ItemsIndex",
        js.Array("id", "date", "itemId", "itemName")
      )
    }

    // Operation
    open.onsuccess = (e: IDBEvent[IDBDatabase]) => {
      import js.Dynamic.{literal => obj}

      val db = e.target.result
      val tx = db.transaction("ShoppingDb", IDBTransactionMode.readwrite)
      val store = tx.objectStore("ShoppingDb")
      val index = store.index("ItemsIndex")

      val date = fromJsDate(new js.Date(js.Date.now()))

      dynModel
        .now()
        .basket
        .foreach(selectedItem =>
          store
            .put(
              obj(
                id = Random.nextInt(10000000).toString,
                date = date,
                itemId = selectedItem.item.id,
                itemName = selectedItem.item.name
              )
            )
          onUnSelectedInBasket(selectedItem.item.id)
        )

      tx.oncomplete = (e: Event) => {
        db.close()
      }

    }

  }

}
