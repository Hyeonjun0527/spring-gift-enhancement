package gift.common.pagination;

public class Pageable {

    private int page;
    private int size;

    public Pageable() {
        this.page = 0;
        this.size = 5;
    }

    public Pageable(int page, int size) {
        if (page < 0) {
            throw new IllegalArgumentException("페이지 번호는 0보다 작을 수 없다.");
        }
        if (size < 1) {
            throw new IllegalArgumentException("페이지 크기는 1보다 작을 수 없다.");
        }
        this.page = page;
        this.size = size;
    }
    public static Pageable of(int page, int size) {
        return new Pageable(page, size);
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        if (page < 0) {
            throw new IllegalArgumentException("페이지 번호는 0보다 작을 수 없다.");
        }
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        if (size < 1) {
            throw new IllegalArgumentException("페이지 크기는 1보다 작을 수 없다.");
        }
        this.size = size;
    }

    public int getOffset() {
        return page * size;
    }
}
