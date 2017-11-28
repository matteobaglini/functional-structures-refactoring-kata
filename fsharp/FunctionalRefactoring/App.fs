module FunctionalRefactoring.App

open Models    

let loadCart cartId =
    match cartId with
    | CartId id when id.Contains "gold" ->
        { cartId = cartId; customerId = CustomerId "gold-customer"; amount = Amount 100m }
    | CartId id when id.Contains "normal" ->
        { cartId = cartId; customerId = CustomerId "normal-customer"; amount = Amount 100m }
    | CartId _ -> missingCart

let half cart =
    match cart.amount with
    | Amount x -> Amount (x / 2m)

let lookupDiscountRule (CustomerId id) =
    if id.Contains "gold"
    then DiscountRule half
    else noDiscount

let updateAmount(cart, Amount discount) =
    let (Amount full) = cart.amount
    { cart with amount = Amount (full - discount) }
        
let save (storage: IStorage<_>) = storage.Flush

let ApplyDiscount (cartId, storage) =
    let cart = loadCart cartId
    if cart <> missingCart
    then
        let rule = lookupDiscountRule cart.customerId
        if rule <> noDiscount
        then
            let (DiscountRule f) = rule
            let discount = f cart
            let updatedCart = updateAmount(cart, discount)
            save storage updatedCart