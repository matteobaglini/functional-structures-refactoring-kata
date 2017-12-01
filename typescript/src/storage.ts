
export interface Storage<T> {
    flush(item: T): void
}
