package dev.thillainirmal.evalforge;
import org.junit.jupiter.api.Test;import java.util.List;import static org.assertj.core.api.Assertions.assertThat;
class RagEvaluatorTest{private final RagEvaluator e=new RagEvaluator();
 private RagEvaluator.Case good(){return new RagEvaluator.Case("How does retry recovery work?",List.of("Retry recovery uses exponential backoff and a circuit breaker."),"Retry recovery uses exponential backoff and a circuit breaker [1].",List.of(1),List.of("exponential backoff","circuit breaker"),300);}
 @Test void passesGroundedAnswer(){var r=e.evaluate(good());assertThat(r.status()).isEqualTo("PASS");assertThat(r.citationPrecision()).isEqualTo(1);}
 @Test void flagsUnsupportedAnswer(){var r=e.evaluate(new RagEvaluator.Case("recovery",List.of("Use backoff."),"A quantum database predicts every outage.",List.of(2),List.of("backoff"),6000));assertThat(r.status()).isEqualTo("FAIL");assertThat(r.flags()).hasSizeGreaterThan(2);}
 @Test void blocksQualityRegression(){var bad=new RagEvaluator.Case("recovery",List.of("Use backoff."),"Unrelated claim.",List.of(9),List.of("backoff"),6000);var c=e.compare(new RagEvaluator.ComparisonRequest(good(),bad));assertThat(c.gate()).isEqualTo("BLOCK");assertThat(c.delta()).isNegative();}
 @Test void toleratesMissingOptionalLists(){var r=e.evaluate(new RagEvaluator.Case("x",null,"x",null,null,0));assertThat(r).isNotNull();}
}
