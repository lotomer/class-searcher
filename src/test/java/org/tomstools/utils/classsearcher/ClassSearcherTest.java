package org.tomstools.utils.classsearcher;


import junit.framework.TestCase;
import org.junit.Test;

public class ClassSearcherTest
  extends TestCase
{

  @Test
  public void testIsContained() {
    assertEquals(true, ClassSearcher.getInstance().isContained("asdf/a/b/c.class", "a.b.c"));
    assertEquals(true, ClassSearcher.getInstance().isContained("asdf/a/b/c.class", "a/b/c"));
    assertEquals(true, ClassSearcher.getInstance().isContained("asdf/a/b/c.java", "a/b/c"));
    assertEquals(false, ClassSearcher.getInstance().isContained("asdf/a/b/c.class", "a\\b\\c"));
    assertEquals(false, ClassSearcher.getInstance().isContained("asdf/a/b/c.java", "a/b/d"));
  }
}