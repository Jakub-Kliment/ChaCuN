package ch.epfl.chacun;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class MyGameStateTest {
    @Test
    void cancelledAnimal() {
        Area<Zone.Meadow> area = new Area<>(new HashSet<>(Set.of(new Zone.Meadow(1, List.of(new Animal(1, Animal.Kind.DEER), new Animal(2, Animal.Kind.DEER), new Animal(3, Animal.Kind.DEER), new Animal(4, Animal.Kind.MAMMOTH), new Animal(5, Animal.Kind.TIGER)), null))), new ArrayList<>(), 0);
        Set<Animal> setAnimal = GameState.cancelAnimalUpdate(area, new HashSet<>());
        Set<Animal> setSearch = Set.of(new Animal(1, Animal.Kind.DEER));
        assertEquals(setSearch, setAnimal);
    }

    @Test
    void cancelledAnimal2() {
        Area<Zone.Meadow> area = new Area<>(new HashSet<>(Set.of(new Zone.Meadow(1, List.of(new Animal(1, Animal.Kind.MAMMOTH), new Animal(2, Animal.Kind.TIGER), new Animal(3, Animal.Kind.MAMMOTH), new Animal(4, Animal.Kind.MAMMOTH), new Animal(5, Animal.Kind.TIGER)), null))), new ArrayList<>(), 0);
        Set<Animal> setAnimal = GameState.cancelAnimalUpdate(area, new HashSet<>());
        Set<Animal> setSearch = Set.of();
        assertEquals(setSearch, setAnimal);
    }

    @Test
    void cancelledAnimal1() {
        Area<Zone.Meadow> area = new Area<>(new HashSet<>(Set.of(new Zone.Meadow(1, List.of(new Animal(1, Animal.Kind.DEER), new Animal(2, Animal.Kind.DEER), new Animal(3, Animal.Kind.DEER), new Animal(4, Animal.Kind.MAMMOTH), new Animal(5, Animal.Kind.MAMMOTH)), null))), new ArrayList<>(), 0);
        Set<Animal> setAnimal = GameState.cancelAnimalUpdate(area, new HashSet<>());
        Set<Animal> setSearch = Set.of();
        assertEquals(setSearch, setAnimal);
    }
    @Test
    void cancelledAnimal3() {
        Area<Zone.Meadow> area = new Area<>(new HashSet<>(Set.of(new Zone.Meadow(1, List.of(new Animal(1, Animal.Kind.DEER), new Animal(2, Animal.Kind.DEER), new Animal(3, Animal.Kind.DEER), new Animal(4, Animal.Kind.TIGER), new Animal(5, Animal.Kind.TIGER)), null))), new ArrayList<>(), 0);
        Set<Animal> setAnimal = GameState.cancelAnimalUpdate(area, new HashSet<>());
        Set<Animal> setSearch = Set.of(new Animal(1, Animal.Kind.DEER), new Animal(2, Animal.Kind.DEER));
        assertEquals(setSearch, setAnimal);
    }
}