package dev.thillainirmal.evalforge;
import org.springframework.web.bind.annotation.*;import java.util.List;
@RestController @RequestMapping("/api/evaluations") public class EvalController{
 private final RagEvaluator evaluator;public EvalController(RagEvaluator e){evaluator=e;}
 @PostMapping public RagEvaluator.Report evaluate(@RequestBody RagEvaluator.Case c){return evaluator.evaluate(c);}
 @PostMapping("/compare") public RagEvaluator.Comparison compare(@RequestBody RagEvaluator.ComparisonRequest r){return evaluator.compare(r);}
 @GetMapping("/demo") public RagEvaluator.Report demo(){return evaluator.evaluate(new RagEvaluator.Case("How does the service recover from dependency failures?",List.of("The circuit breaker opens after repeated dependency failures and retries use exponential backoff.","A half-open probe checks recovery before normal traffic resumes."),"The circuit breaker opens after repeated failures, retries use exponential backoff, and a half-open probe checks recovery [1][2].",List.of(1,2),List.of("circuit breaker","exponential backoff","half-open"),420));}
}

