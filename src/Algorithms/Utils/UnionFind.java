package Algorithms.Utils;

/**
 * Union Finder use by multiple algorithms
 * https://fr.wikipedia.org/wiki/Union-find
 */
public class UnionFind{
    private int[] parent;
    private byte[] rank;
    private int count;

    /**
     * Initializes an empty union–find data structure with n sites
     * 0 through n-1.
     * @param  n the number of sites
     * @throws IllegalArgumentException if n < 0
     */
    public UnionFind(int n) {
        if (n < 0) throw new IllegalArgumentException();
        count = n;
        parent = new int[n];
        rank = new byte[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            rank[i] = 0;
        }
    }

    /**
     * Returns the component identifier for the component containing site {@code p}.
     *
     * @param  p the integer representing one site
     * @return the component identifier for the component containing site {@code p}
     * @throws IndexOutOfBoundsException unless0 <= p < n
     */
    public int find(int p) {
        while (p != parent[p]) {
            parent[p] = parent[parent[p]];
            p = parent[p];
        }
        return p;
    }

    /**
     * Returns true if the the two sites are in the same component.
     * @param  p the integer representing one site
     * @param  q the integer representing the other site
     * @return true if the two sites p and q are in the same component, false otherwise
     * @throws IndexOutOfBoundsException unless
     *         both 0 <= p < n and 0 <= q < n
     */
    public boolean connected(int p, int q) {
        return find(p) == find(q);
    }

    /**
     * Merges the component containing site p with the
     * the component containing site q.
     * @param  p the integer representing one site
     * @param  q the integer representing the other site
     * @throws IndexOutOfBoundsException unless
     *         both 0 <= p < n and 0 <= q < n
     */
    public void union(int p, int q) {
        int rootP = find(p);
        int rootQ = find(q);
        if (rootP == rootQ) return;

        // make root of smaller rank point to root of larger rank
        if      (rank[rootP] < rank[rootQ]) parent[rootP] = rootQ;
        else if (rank[rootP] > rank[rootQ]) parent[rootQ] = rootP;
        else {
            parent[rootQ] = rootP;
            rank[rootP]++;
        }
        count--;
    }
}