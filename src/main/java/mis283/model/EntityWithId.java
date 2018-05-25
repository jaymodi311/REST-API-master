package mis283.model;

public interface EntityWithId<T> {
    T getId();
    void setId(final T id  );
}
