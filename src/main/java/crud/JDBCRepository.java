package crud;

import java.util.List;

public abstract class JDBCRepository<T> {

    public abstract List<T> listAll();

    public abstract T save (T t);

    public abstract int delete(T t);
}
