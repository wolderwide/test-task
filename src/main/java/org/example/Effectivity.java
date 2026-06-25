package org.example;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Setter(AccessLevel.PRIVATE)
public class Effectivity {

    private static final Integer MAX_INT = Integer.MAX_VALUE;

    private List<Range> numbers;
    private String model;

    public Effectivity(String input) {
        String[] tokens = input.split(" ");
        if (tokens.length > 2)
            throw new EffectivityIncorrectException();

        String model = tokens[1];
        if (!(model.startsWith("(") && model.endsWith(")")))
            throw new EffectivityIncorrectException();

        parse(tokens[0], model.substring(1, model.length() - 1));
    }

    public Effectivity(String range, String model) {
        parse(range, model);
    }

    private void parse(String range, String model) {
        if (range == null || model == null
            || range.trim().isEmpty()
                || model.trim().isEmpty()
                || hasInvalidToken(range)) {
            throw new EffectivityIncorrectException();
        }

        setNumbers(simplify(Arrays.stream(range.split(","))
                .map(String::trim)
                .map(String::toUpperCase)
                .map(token -> {
                    if (token.contains("UP")) {
                        String clean = token.replace("-UP", "");
                        int start = Integer.parseInt(clean);
                        return new Range(start, MAX_INT, true);
                    } else if (token.contains("-")) {
                        String[] parts = token.split("-");
                        var begin = Integer.parseInt(parts[0]);
                        var end = Integer.parseInt(parts[1]);
                        var start = Math.min(begin, end);
                        end = Math.max(begin, end);
                        return new Range(start, end, false);
                    } else {
                        var value = Integer.parseInt(token);
                        return new Range(value, value, false);
                    }
                })
                .toList()));

        setModel(model);
    }

    public List<Range> simplify(List<Range> source) {
        if (source == null || source.isEmpty())
            return Collections.emptyList();

        int minUpStart = MAX_INT;
        boolean hasUp = false;

        for (Range r : source) {
            if (r.withUp) {
                hasUp = true;
                if (r.start < minUpStart)
                    minUpStart = r.start;
            }
        }

        Set<Integer> staticNumbers = new TreeSet<>();
        for (Range r : source) {
            if (!r.withUp) {
                int min = Math.min(r.start, r.end);
                int max = Math.max(r.start, r.end);
                for (int i = min; i <= max; i++) {
                    if (!hasUp || i < minUpStart) {
                        staticNumbers.add(i);
                    }
                }
            }
        }

        List<Range> simplified = mergeConsecutive(new ArrayList<>(staticNumbers));

        if (hasUp) {
            if (!simplified.isEmpty()) {
                Range last = simplified.getLast();
                if (last.end + 1 >= minUpStart) {
                    minUpStart = last.start;
                    simplified.removeLast();
                }
            }

            simplified.add(new Range(minUpStart, MAX_INT, true));
        }

        return simplified;
    }

    private List<Range> mergeConsecutive(List<Integer> numbers) {
        List<Range> result = new ArrayList<>();
        if (numbers.isEmpty()) return result;

        int start = numbers.getFirst();
        int prev = start;

        for (int i = 1; i < numbers.size(); i++) {
            int current = numbers.get(i);

            if (current != prev + 1) {
                result.add(new Range(start, prev, false));
                start = current;
            }
            prev = current;
        }
        result.add(new Range(start, prev, false));
        return result;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)",
                numbers.stream().map(Range::toString).collect(Collectors.joining(",")),
                model);
    }

    private boolean hasInvalidToken(String input) {
        String regex = ".*[^0-9,\\-UPup+].*";

        return input.matches(regex);
    }

    @AllArgsConstructor
    private static final class Range {
        private int start;
        private int end;
        private boolean withUp;

        @Override
        public String toString() {
            return withUp ? start + "-UP" : start == end ? String.valueOf(start) : start + "-" + end;
        }
    }
}
