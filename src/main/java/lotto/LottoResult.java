package lotto;

public class LottoResult {
    private int countOf3Match = 0;
    private int countOf4Match = 0;
    private int countOf5Match = 0;
    private int countOf5MatchAndBonus = 0;
    private int countOf6Match = 0;


    public void winningResult(int matchCount, boolean bonusMatch) {
        if (matchCount == 3) {
            countOf3Match++;
        } else if (matchCount == 4) {
            countOf4Match++;
        } else if (matchCount == 5) {
            countOf5Match++;
        } else if (bonusMatch) {
            countOf5MatchAndBonus++;
        } else if (matchCount == 6) {
            countOf6Match++;
        }
    }


    public int getCountOf3Match() {
        return countOf3Match;
    }


    public int getCountOf4Match() {
        return countOf4Match;
    }


    public int getCountOf5Match() {
        return countOf5Match;
    }


    public int getCountOf5MatchAndBonus() {
        return countOf5MatchAndBonus;
    }


    public int getCountOf6Match() {
        return countOf6Match;
    }
}
