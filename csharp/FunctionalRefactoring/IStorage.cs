namespace FunctionalRefactoring
{
    public interface IStorage<T>
    {
        void Flush(T item);
    }
}