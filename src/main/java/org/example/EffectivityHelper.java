package org.example;

import java.util.ArrayList;
import java.util.List;

import static org.example.Effectivity.simplify;

public class EffectivityHelper {
    public static Effectivity union(String first, String second) {
        var effectivityFirst = new Effectivity(first);
        var effectivitySecond = new Effectivity(second);

        if (!effectivityFirst.getModel().equals(effectivitySecond.getModel()))
            throw new EffectivityIncorrectException();

        List<Effectivity.Range> ranges = new ArrayList<>(effectivityFirst.getNumbers().size() + effectivitySecond.getNumbers().size());
        ranges.addAll(effectivityFirst.getNumbers());
        ranges.addAll(effectivitySecond.getNumbers());

        return new Effectivity()
                .setNumbers(simplify(ranges))
                .setModel(effectivityFirst.getModel());
    }

    public static Effectivity subtract(String first, String second) {
        Effectivity minuend = new Effectivity(first);
        Effectivity subtrahend = new Effectivity(second);
        
        List<Effectivity.Range> minuendRanges = minuend.getNumbers();
        List<Effectivity.Range> subtrahendRanges = subtrahend.getNumbers();

        for (Effectivity.Range sub : subtrahendRanges) {
            List<Effectivity.Range> nextBase = new ArrayList<>();

            long subEnd = sub.isWithUp() ? Long.MAX_VALUE : Math.max(sub.getStart(), sub.getEnd());
            long subMin = sub.isWithUp() ? sub.getStart() : Math.min(sub.getStart(), sub.getEnd());

            for (Effectivity.Range base : minuendRanges) {
                long baseEnd = base.isWithUp() ? Long.MAX_VALUE : Math.max(base.getStart(), base.getEnd());
                long baseMin = base.isWithUp() ? base.getStart() : Math.min(base.getStart(), base.getEnd());

                if (subEnd < baseMin || subMin > baseEnd) {
                    nextBase.add(base);
                }
                else {
                    if (baseMin < subMin) {
                        nextBase.add(new Effectivity.Range((int) baseMin, (int) (subMin - 1), false));
                    }
                    if (baseEnd > subEnd) {
                        if (base.isWithUp()) {
                            nextBase.add(new Effectivity.Range((int) (subEnd + 1), Integer.MAX_VALUE, true));
                        } else {
                            nextBase.add(new Effectivity.Range((int) (subEnd + 1), (int) baseEnd, false));
                        }
                    }
                }
            }
            minuendRanges = nextBase;
        }

        if (minuendRanges.isEmpty())
            return null;
        return new Effectivity()
                .setNumbers(simplify(minuendRanges))
                .setModel(minuend.getModel());
    }

    public static Effectivity intersect(String first, String second) {
        Effectivity firstE = new Effectivity(first);
        Effectivity secondE = new Effectivity(second);

        List<Effectivity.Range> rangeFirst = firstE.getNumbers();
        List<Effectivity.Range> rangeSecond = secondE.getNumbers();
        List<Effectivity.Range> result = new ArrayList<>();

        for (Effectivity.Range a : rangeFirst) {
            long aMin = a.isWithUp() ? a.getStart() : Math.min(a.getStart(), a.getEnd());
            long aMax = a.isWithUp() ? Long.MAX_VALUE : Math.max(a.getStart(), a.getEnd());

            for (Effectivity.Range b : rangeSecond) {
                long bMin = b.isWithUp() ? b.getStart() : Math.min(b.getStart(), b.getEnd());
                long bMax = b.isWithUp() ? Long.MAX_VALUE : Math.max(b.getStart(), b.getEnd());

                long intersectMin = Math.max(aMin, bMin);
                long intersectMax = Math.min(aMax, bMax);

                if (intersectMin <= intersectMax) {
                    boolean isResultUnbounded = (a.isWithUp() && b.isWithUp());

                    if (isResultUnbounded) {
                        result.add(new Effectivity.Range((int) intersectMin, Integer.MAX_VALUE, true));
                    } else {
                        result.add(new Effectivity.Range((int) intersectMin, (int) intersectMax, false));
                    }
                }
            }
        }

        if (result.isEmpty())
            return null;
        return new Effectivity()
                .setNumbers(simplify(result))
                .setModel(firstE.getModel());
    }

    public static boolean include(String first, String second) {
        Effectivity remainder = subtract(second, first);

        return remainder == null;
    }

    public static boolean includePart(String first, String second) {
        Effectivity result = intersect(first, second);

        return result != null;
    }
}
