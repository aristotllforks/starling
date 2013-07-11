package com.opengamma.integration.tool.enginedebugger.node;

public interface TreeTableNode {
  Object getChildAt(int index);
  int getChildCount();
  int getIndexOfChild(Object child);
  Object getColumn(int column);
}
