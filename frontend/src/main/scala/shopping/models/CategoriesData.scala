package shopping.models

object CategoriesData {

  val produce = Category(cid = "1", "Produce", "Fruits & Vegetables")
  val meatAndSeadFood = Category(
    cid = "2",
    "Meat & Seafood",
    "Chicken, beef, fish, shrimp, deli meats"
  )
  val bakery =
    Category(cid = "3", "Bakery", "Bread, bagels, muffins, tortillas, cakes")
  val beverages = Category(
    cid = "4",
    "Beverages",
    "Coffee, tea, juice, soda, water, beer, wine"
  )
  val frozenFoods = Category(
    cid = "5",
    "Frozen Foods",
    "Ice cream, frozen vegetables, pizzas, ready meals. "
  )
  val pantry = Category(
    cid = "6",
    "Pantry/Dry Goods",
    "Pasta, rice, cereal, flour, sugar, spices, canned goods, oils, sauces."
  )
  val dairy = Category(
    cid = "11",
    "Dairy & Refrigerated",
    "Milk, cheese, yogurt, butter, eggs, prepared meals"
  )

  val houseHold = Category(
    cid = "15",
    "Household/Cleaning",
    "Detergent, paper towels, trash bags, sponges."
  )

  private val defaultCategories = List(
    produce,
    meatAndSeadFood,
    bakery,
    beverages,
    frozenFoods,
    pantry,
    dairy
  )

  var all: List[Category] = defaultCategories

}
