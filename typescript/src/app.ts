import { isEqual } from 'lodash'

import { Storage } from './storage'
import { CartId, MissingCart, NoDiscount, Cart, CustomerId, DiscountRule, Amount } from './models'

export const applyDiscount = (cartId: CartId, storage: Storage<Cart>): void => {

  const cart = loadCart(cartId)

  if (!isEqual(cart, MissingCart)) {
    const rule = lookupDiscountRule(cart.customerId)
    if (!isEqual(rule, NoDiscount)) {
      const discount = rule(cart)
      const updatedCart = updateAmount(cart, discount)
      save(updatedCart, storage)
    }
  }
}

const loadCart = (id: CartId): Cart => {
  if (id.includes('gold'))
    return {
      id: id,
      customerId: 'gold-customer',
      amount: 100
    }
  if (id.includes('normal'))
    return {
      id: id,
      customerId: 'normal-customer',
      amount: 100
    }
  return MissingCart
}

const lookupDiscountRule = (id: CustomerId): DiscountRule => {
  if (id.includes('gold')) return half
  return NoDiscount
}

const updateAmount = (cart: Cart, discount: Amount): Cart => {
  return {
    ...cart,
    amount: cart.amount - discount
  }
}

const save = (cart: Cart, storage: Storage<Cart>) => {
  storage.flush(cart)
}

const half = (cart: Cart): Amount => {
  return cart.amount / 2.0
}
