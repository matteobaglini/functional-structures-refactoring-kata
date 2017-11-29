import 'mocha'
import { assert } from 'chai'

import { CartId, Cart } from '../src/models'
import { applyDiscount } from '../src/app'
import { SpyStorage } from './stub/storage'

describe('Functional Refactoring', () => {

  it('Happy Path', () => {
    const cartId: CartId = 'some-gold-cart' 
    const storage = new SpyStorage()
  
    applyDiscount(cartId, storage)
  
    const expected: Cart = {
      id: 'some-gold-cart',
      customerId: 'gold-customer',
      amount: 50
    }
    
    assert.deepEqual(storage.saved, expected)

  })

  
  it('No discount', () => {
    const cartId: CartId = 'some-normal-cart' 
    const storage = new SpyStorage()
  
    applyDiscount(cartId, storage)
  
    assert.isUndefined(storage.saved)

  })

  it('Missing Cart', () => {
    const cartId: CartId = 'missing-cart' 
    const storage = new SpyStorage()
  
    applyDiscount(cartId, storage)
  
    assert.isUndefined(storage.saved)

  })
})
