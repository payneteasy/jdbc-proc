package com.googlecode.jdbcproc.daofactory.impl.block.impl;

import com.googlecode.jdbcproc.daofactory.ResultSetAdapter;
import com.googlecode.jdbcproc.daofactory.impl.block.IResultSetConverterContext;
import org.junit.Assert;
import org.junit.Test;

import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Multi-level @OneToMany grouping. Entities are compared by name only (as in
 * the integration-test domain: Grandfather/Father/Boy), so a child with the
 * same name may legitimately appear under two different parents.
 */
public class ResultSetConverterBlockEntityOneToMany2xListTest {

    /** 3 levels: control case, same shape as the integration test testAncestry. */
    @Test
    public void threeLevels_sameChildNameUnderNewParent() throws SQLException {
        List<Node> roots = convert(3, new String[][] {
                {"Tom", "John", "Jimmy"},
                {"Sam", "John", "Jimmy"},
        });
        Assert.assertEquals(names("Tom", "Sam"), names(roots));
        Assert.assertEquals(names("John"), names(roots.get(0).children));
        Assert.assertEquals(names("John"), names(roots.get(1).children));
        Assert.assertEquals(names("Jimmy"), names(roots.get(0).children.get(0).children));
        Assert.assertEquals(names("Jimmy"), names(roots.get(1).children.get(0).children));
    }

    /** 4 levels: level-3 parent changes while level-4 child keeps the same name. */
    @Test
    public void fourLevels_sameChildNameUnderNewThirdLevelParent() throws SQLException {
        List<Node> roots = convert(4, new String[][] {
                {"Family", "Tom", "John", "Jimmy"},
                {"Family", "Tom", "Bob",  "Jimmy"},
        });
        Assert.assertEquals(names("Family"), names(roots));
        Node tom = roots.get(0).children.get(0);
        Assert.assertEquals(names("John", "Bob"), names(tom.children));
        Assert.assertEquals(names("Jimmy"), names(tom.children.get(0).children));
        Assert.assertEquals("Bob must get his own Jimmy", names("Jimmy"), names(tom.children.get(1).children));
    }

    /** 5 levels: level-4 parent changes while level-5 child keeps the same name. */
    @Test
    public void fiveLevels_sameChildNameUnderNewFourthLevelParent() throws SQLException {
        List<Node> roots = convert(5, new String[][] {
                {"Clan", "Family", "Tom", "John", "Jimmy"},
                {"Clan", "Family", "Tom", "Bob",  "Jimmy"},
        });
        Node tom = roots.get(0).children.get(0).children.get(0);
        Assert.assertEquals(names("John", "Bob"), names(tom.children));
        Assert.assertEquals("Bob must get his own Jimmy", names("Jimmy"), names(tom.children.get(1).children));
    }

    // ---- helpers ------------------------------------------------------------

    @SuppressWarnings("unchecked")
    private static List<Node> convert(int levels, String[][] rows) throws SQLException {
        List<OneToManyLink> links = new ArrayList<OneToManyLink>();
        for (int i = 1; i <= levels; i++) {
            links.add(new NodeLink(i));
        }
        ResultSetConverterBlockEntityOneToMany2xList block = new ResultSetConverterBlockEntityOneToMany2xList(links);
        return (List<Node>) (List) block.convertResultSet(new Ctx(new RowsResultSet(rows)));
    }

    private static List<String> names(String... names) {
        return Arrays.asList(names);
    }

    private static List<String> names(List<Node> nodes) {
        List<String> ret = new ArrayList<String>();
        for (Node n : nodes) {
            ret.add(n.name);
        }
        return ret;
    }

    /** Entity: equals/hashCode by name only, like Grandfather/Father in IT tests. */
    static class Node {
        final String name;
        List<Node> children = Collections.emptyList();

        Node(String name) { this.name = name; }

        @Override public boolean equals(Object o) {
            return o instanceof Node && Objects.equals(name, ((Node) o).name);
        }
        @Override public int hashCode() { return Objects.hashCode(name); }
        @Override public String toString() { return name + children; }
    }

    /** Link that loads a Node from one column, bypassing reflection. */
    static class NodeLink extends OneToManyLink {
        private final int column;

        NodeLink(int column) {
            super(Node.class, Collections.<EntityPropertySetter>emptyList(), Collections.<OneToOneLink>emptyList(), null);
            this.column = column;
        }

        @Override public Object createEmptyEntity() { return new Node(null); }

        @Override public Object loadEntity(ResultSet rs) {
            try {
                return new Node(rs.getString(column));
            } catch (SQLException e) {
                throw new IllegalStateException(e);
            }
        }

        @SuppressWarnings("unchecked")
        @Override public void setChildren(Object entity, List<Object> children) {
            ((Node) entity).children = (List<Node>) (List) children;
        }
    }

    static class RowsResultSet extends ResultSetAdapter {
        private final String[][] rows;
        private int idx = -1;

        RowsResultSet(String[][] rows) { this.rows = rows; }

        @Override public boolean next() { return ++idx < rows.length; }
        @Override public String getString(int columnIndex) { return rows[idx][columnIndex - 1]; }
    }

    static class Ctx implements IResultSetConverterContext {
        private final ResultSet rs;
        Ctx(ResultSet rs) { this.rs = rs; }
        public ResultSet getResultSet() { return rs; }
        public CallableStatement getCallableStatement() { return null; }
        public DataSource getDataSource() { return null; }
        public Runnable getOnCloseCleanup() { return null; }
    }
}
