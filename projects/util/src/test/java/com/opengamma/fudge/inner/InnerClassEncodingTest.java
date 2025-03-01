/**
 * Copyright (C) 2011 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.fudge.inner;

import static com.google.common.collect.Lists.newArrayList;
import static com.opengamma.util.fudgemsg.AutoFudgable.autoFudge;
import static org.testng.Assert.assertEquals;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.testng.annotations.Test;

import com.opengamma.util.test.AbstractFudgeBuilderTestCase;
import com.opengamma.util.test.TestGroup;

/**
 * Test Fudge encoding.
 */
@Test(groups = TestGroup.UNIT)
public class InnerClassEncodingTest extends AbstractFudgeBuilderTestCase {

  Random _generator = new Random(System.currentTimeMillis());

  /**
   *
   */
  public void testInnerWithoutcontext() {
    final TestOuterClass inner = new TestOuterClass() {
    };

    final TestOuterClass cycled = cycleObjectOverBytes(autoFudge(inner)).object();
    for (int i = 0; i < 100; i++) {
      final double randomArg = _generator.nextDouble();
      assertEquals(inner.eval(randomArg), cycled.eval(randomArg));
    }
  }

  /**
   *
   */
  public void testInnerWithPrimitiveContext() {
    final double someContext = _generator.nextDouble();
    final TestOuterClass inner = new TestOuterClass() {
      @Override
      public double eval(final double arg) {
        return arg * someContext;
      }
    };

    final TestOuterClass cycled = cycleObjectOverBytes(autoFudge(inner)).object();
    for (int i = 0; i < 100; i++) {
      final double randomArg = _generator.nextDouble();
      assertEquals(inner.eval(randomArg), cycled.eval(randomArg));
    }
  }

  /**
   *
   */
  public void testInnerWithTwoPrimitiveContexts() {
    final double someContextA = 1.0;
    final double someContextB = 2.0;
    final TestOuterClass inner = new TestOuterClass() {
      @Override
      public double eval(final double arg) {
        return arg * someContextA + someContextB;
      }
    };

    final TestOuterClass cycled = cycleObjectOverBytes(autoFudge(inner)).object();
    for (int i = 0; i < 100; i++) {
      final double randomArg = _generator.nextDouble();
      assertEquals(inner.eval(randomArg), cycled.eval(randomArg));
    }
  }

  /**
   *
   */
  public void testInnerWithArrayOfPrimitivesContext() {

    final int count = _generator.nextInt(100);
    final double[] someContext = new double[count];

    for (int j = 0; j < count; j++) {
      someContext[j] = _generator.nextDouble();
    }

    final TestOuterClass inner = new TestOuterClass() {
      @Override
      public double eval(final double arg) {
        double sum = arg;
        for (final double d : someContext) {
          sum += d;
        }
        return sum;
      }
    };

    final TestOuterClass cycled = cycleObjectOverBytes(autoFudge(inner)).object();

    for (int i = 0; i < 100; i++) {
      final double randomArg = _generator.nextDouble();
      assertEquals(inner.eval(randomArg), cycled.eval(randomArg));
    }
  }

  /**
   *
   */
  public void testInnerWithPojoContext() {
    final ContextPOJO someContext = new ContextPOJO();
    someContext.setValue(_generator.nextDouble());

    final TestOuterClass inner = new TestOuterClass() {
      @Override
      public double eval(final double arg) {
        return arg * someContext.getValue();
      }
    };

    final TestOuterClass cycled = cycleObjectOverBytes(autoFudge(inner)).object();
    for (int i = 0; i < 100; i++) {
      final double randomArg = _generator.nextDouble();
      assertEquals(inner.eval(randomArg), cycled.eval(randomArg));
    }
  }

  /**
   * This fails because fudge can't serialize arrays of something other than primitives.
   */
  @Test(enabled = false)
  public void testInnerWithArrayOfPojosContext() {
    final int count = _generator.nextInt(100);
    final ContextPOJO[] someContext = new ContextPOJO[count];

    for (int j = 0; j < count; j++) {
      someContext[j] = new ContextPOJO();
      someContext[j].setValue(_generator.nextDouble());
    }

    final TestOuterClass inner = new TestOuterClass() {
      @Override
      public double eval(final double arg) {
        double sum = arg;
        for (final ContextPOJO pojo : someContext) {
          sum += pojo.getValue();
        }
        return sum;
      }
    };

    final TestOuterClass cycled = cycleObjectOverBytes(autoFudge(inner)).object();

    for (int i = 0; i < 100; i++) {
      final double randomArg = _generator.nextDouble();
      assertEquals(inner.eval(randomArg), cycled.eval(randomArg));
    }
  }

  /**
   *
   */
  public void testInnerWithListOfPojosContext() {
    final int count = _generator.nextInt(100);
    final List<ContextPOJO> someContext = newArrayList();

    for (int j = 0; j < count; j++) {
      final ContextPOJO pojo = new ContextPOJO();
      pojo.setValue(_generator.nextDouble());
      someContext.add(pojo);
    }

    final TestOuterClass inner = new TestOuterClass() {
      @Override
      public double eval(final double arg) {
        double sum = arg;
        for (final ContextPOJO pojo : someContext) {
          sum += pojo.getValue();
        }
        return sum;
      }
    };

    final TestOuterClass cycled = cycleObjectOverBytes(autoFudge(inner)).object();

    for (int i = 0; i < 100; i++) {
      final double randomArg = _generator.nextDouble();
      assertEquals(inner.eval(randomArg), cycled.eval(randomArg));
    }
  }

  /**
   *
   */
  public void testInnerWithContextCopiedFromEnclosingClass() {
    final double someContext = _someOuterContext;
    final TestOuterClass inner = new TestOuterClass() {
      @Override
      public double eval(final double arg) {
        return arg * someContext;
      }
    };

    final TestOuterClass cycled = cycleObjectOverBytes(autoFudge(inner)).object();
    for (int i = 0; i < 100; i++) {
      final double randomArg = _generator.nextDouble();
      assertEquals(inner.eval(randomArg), cycled.eval(randomArg));
    }
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  /**
   *
   */
  public void testInnerImplementingIfaceWithoutContext() {
    final TestOuterInterface inner = new TestOuterInterface() {
      @Override
      public double eval(final double arg) {
        return arg;
      }
    };

    final TestOuterInterface cycled = cycleObjectOverBytes(autoFudge(inner)).object();
    for (int i = 0; i < 100; i++) {
      final double randomArg = _generator.nextDouble();
      assertEquals(inner.eval(randomArg), cycled.eval(randomArg));
    }
  }

  /**
   *
   */
  public void testInnerImplementingIfaceWithPrimitiveContext() {
    final double someContext = _generator.nextDouble();
    final TestOuterInterface inner = new TestOuterInterface() {
      @Override
      public double eval(final double arg) {
        return arg * someContext;
      }
    };

    final TestOuterInterface cycled = cycleObjectOverBytes(autoFudge(inner)).object();
    for (int i = 0; i < 100; i++) {
      final double randomArg = _generator.nextDouble();
      assertEquals(inner.eval(randomArg), cycled.eval(randomArg));
    }
  }

  /**
   *
   */
  public void testInnerImplementingIfaceWithArrayOfPrimitivesContext() {

    final int count = _generator.nextInt(100);
    final double[] someContext = new double[count];

    for (int j = 0; j < count; j++) {
      someContext[j] = _generator.nextDouble();
    }

    final TestOuterInterface inner = new TestOuterInterface() {
      @Override
      public double eval(final double arg) {
        double sum = arg;
        for (final double d : someContext) {
          sum += d;
        }
        return sum;
      }
    };

    final TestOuterInterface cycled = cycleObjectOverBytes(autoFudge(inner)).object();

    for (int i = 0; i < 100; i++) {
      final double randomArg = _generator.nextDouble();
      assertEquals(inner.eval(randomArg), cycled.eval(randomArg));
    }
  }

  /**
   *
   */
  public void testInnerImplementingIfaceWithPojoContext() {
    final ContextPOJO someContext = new ContextPOJO();
    someContext.setValue(_generator.nextDouble());

    final TestOuterInterface inner = new TestOuterInterface() {
      @Override
      public double eval(final double arg) {
        return arg * someContext.getValue();
      }
    };

    final TestOuterInterface cycled = cycleObjectOverBytes(autoFudge(inner)).object();
    for (int i = 0; i < 100; i++) {
      final double randomArg = _generator.nextDouble();
      assertEquals(inner.eval(randomArg), cycled.eval(randomArg));
    }
  }

  /**
   * This fails because fudge can't serialize arrays of something other than primitives.
   */
  @Test(enabled = false)
  public void testInnerImplementingIfaceWithArrayOfPojosContext() {
    final int count = _generator.nextInt(100);
    final ContextPOJO[] someContext = new ContextPOJO[count];

    for (int j = 0; j < count; j++) {
      someContext[j] = new ContextPOJO();
      someContext[j].setValue(_generator.nextDouble());
    }

    final TestOuterInterface inner = new TestOuterInterface() {
      @Override
      public double eval(final double arg) {
        double sum = arg;
        for (final ContextPOJO pojo : someContext) {
          sum += pojo.getValue();
        }
        return sum;
      }
    };

    final TestOuterInterface cycled = cycleObjectOverBytes(autoFudge(inner)).object();

    for (int i = 0; i < 100; i++) {
      final double randomArg = _generator.nextDouble();
      assertEquals(inner.eval(randomArg), cycled.eval(randomArg));
    }
  }

  /**
   *
   */
  public void testInnerImplementingIfaceWithListOfPojosContext() {
    final int count = _generator.nextInt(100);
    final List<ContextPOJO> someContext = newArrayList();

    for (int j = 0; j < count; j++) {
      final ContextPOJO pojo = new ContextPOJO();
      pojo.setValue(_generator.nextDouble());
      someContext.add(pojo);
    }

    final TestOuterInterface inner = new TestOuterInterface() {
      @Override
      public double eval(final double arg) {
        double sum = arg;
        for (final ContextPOJO pojo : someContext) {
          sum += pojo.getValue();
        }
        return sum;
      }
    };

    final TestOuterInterface cycled = cycleObjectOverBytes(autoFudge(inner)).object();

    for (int i = 0; i < 100; i++) {
      final double randomArg = _generator.nextDouble();
      assertEquals(inner.eval(randomArg), cycled.eval(randomArg));
    }
  }

  /** */
  double _someOuterContext = _generator.nextDouble();

  /**
   *
   */
  public void testInnerImplementingIfaceWithContextCopiedFromEnclosingClass() {
    final double someContext = _someOuterContext;
    final TestOuterInterface inner = new TestOuterInterface() {
      @Override
      public double eval(final double arg) {
        return arg * someContext;
      }
    };

    final TestOuterInterface cycled = cycleObjectOverBytes(autoFudge(inner)).object();
    for (int i = 0; i < 100; i++) {
      final double randomArg = _generator.nextDouble();
      assertEquals(inner.eval(randomArg), cycled.eval(randomArg));
    }
  }

  /**
   *
   */
  public void testACollectionWhichIsInnerClass() {
    final Map<Byte, Byte> map = Collections.unmodifiableMap(new HashMap<Byte, Byte>() {
      private static final long serialVersionUID = 1L;
      {
        this.put((byte) 1, (byte) 2);
      }
    });
    @SuppressWarnings("rawtypes")
    final Map cycled = cycleObjectOverBytes(map);

    assertEquals(cycled.get((byte) 1), (byte) 2);
  }

}
