module FunctionalRefactoring.Models

type IStorage<'a> =
    abstract Flush: 'a -> unit

type Amount = Amount of decimal
type CartId = CartId of string
type CustomerId = CustomerId of string

type Cart = { cartId: CartId; customerId: CustomerId; amount: Amount } 

[<ReferenceEquality>]
type DiscountRule = DiscountRule of (Cart -> Amount)

let noDiscount = DiscountRule (fun _ -> invalidOp "no discount")

let missingCart = { cartId = CartId ""; customerId = CustomerId ""; amount = Amount 0m }