package org.shitianren.hw2;

import org.shared.chess.AbstractStateChangerAllTest;
import org.shared.chess.StateChanger;
import org.shitianren.hw2.StateChangerImpl;

public class StateChangerImplAllTest extends AbstractStateChangerAllTest {
  @Override
  public StateChanger getStateChanger() {
    return new StateChangerImpl();
  }
}