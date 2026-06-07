import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TriangleTest {

    // ── Boundary Value Analysis ──────────────────────────────────────────────
    // Nominal value for unchanged variables: 100
    // BVA points: 0 (invalid), 1 (min), 2 (min+1), 100 (nominal), 199 (max-1), 200 (max), 201 (invalid)

    // Variable a boundaries (b=100, c=100)
    @Test void bva_a_belowMin()    { assertEquals("Value of a is not in the range of permitted values.", Triangle.classify(0,   100, 100)); }
    @Test void bva_a_min()         { assertEquals("Isosceles",    Triangle.classify(1,   100, 100)); }
    @Test void bva_a_minPlusOne()  { assertEquals("Isosceles",    Triangle.classify(2,   100, 100)); }
    @Test void bva_a_nominal()     { assertEquals("Equilateral",  Triangle.classify(100, 100, 100)); }
    @Test void bva_a_maxMinusOne() { assertEquals("Isosceles",    Triangle.classify(199, 100, 100)); }
    @Test void bva_a_max()         { assertEquals("NotATriangle", Triangle.classify(200, 100, 100)); }
    @Test void bva_a_aboveMax()    { assertEquals("Value of a is not in the range of permitted values.", Triangle.classify(201, 100, 100)); }

    // Variable b boundaries (a=100, c=100)
    @Test void bva_b_belowMin()    { assertEquals("Value of b is not in the range of permitted values.", Triangle.classify(100, 0,   100)); }
    @Test void bva_b_min()         { assertEquals("Isosceles",    Triangle.classify(100, 1,   100)); }
    @Test void bva_b_minPlusOne()  { assertEquals("Isosceles",    Triangle.classify(100, 2,   100)); }
    @Test void bva_b_maxMinusOne() { assertEquals("Isosceles",    Triangle.classify(100, 199, 100)); }
    @Test void bva_b_max()         { assertEquals("NotATriangle", Triangle.classify(100, 200, 100)); }
    @Test void bva_b_aboveMax()    { assertEquals("Value of b is not in the range of permitted values.", Triangle.classify(100, 201, 100)); }

    // Variable c boundaries (a=100, b=100)
    @Test void bva_c_belowMin()    { assertEquals("Value of c is not in the range of permitted values.", Triangle.classify(100, 100, 0));   }
    @Test void bva_c_min()         { assertEquals("Isosceles",    Triangle.classify(100, 100, 1));   }
    @Test void bva_c_minPlusOne()  { assertEquals("Isosceles",    Triangle.classify(100, 100, 2));   }
    @Test void bva_c_maxMinusOne() { assertEquals("Isosceles",    Triangle.classify(100, 100, 199)); }
    @Test void bva_c_max()         { assertEquals("NotATriangle", Triangle.classify(100, 100, 200)); }
    @Test void bva_c_aboveMax()    { assertEquals("Value of c is not in the range of permitted values.", Triangle.classify(100, 100, 201)); }

    // ── Equivalence Partitioning ─────────────────────────────────────────────

    @Test void ep_equilateral()   { assertEquals("Equilateral", Triangle.classify(5, 5, 5)); }
    @Test void ep_isosceles_ab()  { assertEquals("Isosceles",   Triangle.classify(5, 5, 8)); } // a == b
    @Test void ep_isosceles_bc()  { assertEquals("Isosceles",   Triangle.classify(8, 5, 5)); } // b == c
    @Test void ep_isosceles_ac()  { assertEquals("Isosceles",   Triangle.classify(5, 8, 5)); } // a == c
    @Test void ep_scalene()       { assertEquals("Scalene",     Triangle.classify(3, 4, 5)); }
    @Test void ep_notATriangle()  { assertEquals("NotATriangle",Triangle.classify(1, 2, 10)); }

    // ── Error Guessing ───────────────────────────────────────────────────────

    @Test void eg_negativeA()    { assertEquals("Value of a is not in the range of permitted values.", Triangle.classify(-1, 5, 5)); }
    @Test void eg_negativeB()    { assertEquals("Value of b is not in the range of permitted values.", Triangle.classify(5, -1, 5)); }
    @Test void eg_negativeC()    { assertEquals("Value of c is not in the range of permitted values.", Triangle.classify(5, 5, -1)); }
    @Test void eg_allZero()      { assertEquals("Value of a is not in the range of permitted values.", Triangle.classify(0, 0, 0)); }

    // ── Branch Coverage ──────────────────────────────────────────────────────
    // hit each triangle-inequality branch separately

    @Test void bc_notATriangle_aGeBC() { assertEquals("NotATriangle", Triangle.classify(10, 1, 1)); } // a >= b+c
    @Test void bc_notATriangle_bGeAC() { assertEquals("NotATriangle", Triangle.classify(1, 10, 1)); } // b >= a+c
    @Test void bc_notATriangle_cGeAB() { assertEquals("NotATriangle", Triangle.classify(1, 1, 10)); } // c >= a+b
}
