import { Storage } from '../../src/storage'
import { Cart } from '../../src/models'

export class SpyStorage implements Storage<Cart>{

  public saved: Cart

  flush(item: Cart): void {
    this.saved = item
  }
  
}
