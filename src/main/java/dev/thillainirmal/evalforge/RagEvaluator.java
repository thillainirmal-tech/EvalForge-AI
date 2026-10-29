package dev.thillainirmal.evalforge;
import org.springframework.stereotype.Service;import java.util.*;import java.util.stream.Collectors;
@Service public class RagEvaluator{
 private static final Set<String> STOP=Set.of("the","a","an","and","or","to","of","in","is","are","for","with","on","by","this","that","it","be","as","from");
 public Report evaluate(Case c){
  Set<String> context=tokens(String.join(" ",c.contexts()));Set<String> answer=tokens(c.answer());Set<String> question=tokens(c.question());
  double grounded=ratio(intersection(answer,context).size(),answer.size());double relevance=ratio(intersection(context,question).size(),question.size());
  long validCitations=c.citations().stream().filter(x->x>=1&&x<=c.contexts().size()).distinct().count();double citationPrecision=ratio(validCitations,c.citations().size());
  double completeness=c.expectedTerms().isEmpty()?1:ratio(c.expectedTerms().stream().map(String::toLowerCase).filter(t->c.answer().toLowerCase().contains(t)).count(),c.expectedTerms().size());
  double latency=Math.max(0,Math.min(1,1-(c.latencyMs()/5000.0)));double overall=round(grounded*.35+relevance*.15+citationPrecision*.20+completeness*.20+latency*.10);
  List<String> flags=new ArrayList<>();if(grounded<.55)flags.add("Possible unsupported claims");if(citationPrecision<1)flags.add("Invalid or duplicate citations");if(completeness<.7)flags.add("Expected answer elements missing");if(c.latencyMs()>3000)flags.add("Latency budget exceeded");if(flags.isEmpty())flags.add("No deterministic quality gate violations");
  return new Report(overall,round(grounded),round(relevance),round(citationPrecision),round(completeness),round(latency),overall>=.75?"PASS":overall>=.55?"WARN":"FAIL",List.copyOf(flags));
 }
 public Comparison compare(ComparisonRequest r){Report baseline=evaluate(r.baseline());Report candidate=evaluate(r.candidate());double delta=round(candidate.overall()-baseline.overall());return new Comparison(baseline,candidate,delta,delta>=-.03&&candidate.status().equals("PASS")?"ACCEPT":"BLOCK",delta<-.03?"Quality regression exceeds 3%":"Candidate stays within the quality budget");}
 private Set<String> tokens(String s){return Arrays.stream(s.toLowerCase().replaceAll("[^a-z0-9 ]"," ").split("\\s+")).filter(x->x.length()>2&&!STOP.contains(x)).collect(Collectors.toSet());}
 private <T> Set<T> intersection(Set<T>a,Set<T>b){var c=new HashSet<>(a);c.retainAll(b);return c;}private double ratio(long a,long b){return b==0?1:(double)a/b;}private double round(double d){return Math.round(d*1000)/1000.0;}
 public record Case(String question,List<String> contexts,String answer,List<Integer> citations,List<String> expectedTerms,long latencyMs){public Case{contexts=contexts==null?List.of():List.copyOf(contexts);citations=citations==null?List.of():List.copyOf(citations);expectedTerms=expectedTerms==null?List.of():List.copyOf(expectedTerms);}}
 public record Report(double overall,double groundedness,double contextRelevance,double citationPrecision,double completeness,double latencyScore,String status,List<String> flags){}
 public record ComparisonRequest(Case baseline,Case candidate){}
 public record Comparison(Report baseline,Report candidate,double delta,String gate,String explanation){}
}

