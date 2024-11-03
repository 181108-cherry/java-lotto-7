package lotto;

import camp.nextstep.edu.missionutils.Console;
import camp.nextstep.edu.missionutils.Randoms;

import java.util.ArrayList;
import java.util.List;

public class Application {
    public static void main(String[] args) {
        // TODO: 프로그램 구현
        int money;
        List<Lotto> lottoList = new ArrayList<>();
        List<Integer> winningNumbers;
        int bonusNumber;

        while (true) {
            try {
                money = getMoney();
                exception(money);
                break;
            } catch (IllegalArgumentException error) {
                System.out.println("[ERROR]" + error.getMessage());
            }
        }
        lottoPurchase();

        while (true) {
            try {
                winningNumbers = inputWinningNumbers();
                break;
            } catch (IllegalArgumentException error) {
                System.out.println("[ERROR]" + error.getMessage());
            }
        }

        while (true) {
            try {
                bonusNumber = inputBonusNumber(winningNumbers);
                break;
            } catch (NumberFormatException error) {
                System.out.println("[ERROR]" + " 1개의 숫자 이외의 문자는 입력이 안됩니다.");
            } catch (IllegalArgumentException error) {
                System.out.println("[ERROR]" + error.getMessage());
            }
        }

        LottoResult result = calculateResult(lottoList, winningNumbers, bonusNumber);
        outputResult(result);

        profitRateResult(money, result.totalWinningPrize());
    }

    private static void lottoPurchase() {
        int money = getMoney();
        exception(money);
        int count = countLotto(money);
        outputCount(count);
        List<Lotto> lottoList = generateLotto(count);
        outputnumbers(lottoList);
    }

    private static List<Lotto> generateLotto(int count) {
        List<Lotto> lottoList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            List<Integer> numbers = randomNumbers();
            Lotto lotto = new Lotto(numbers);
            lottoList.add(lotto);
        }
        return lottoList;
    }

    private static final int LOTTO_PRICE = 1000;
    private static final int MIN_LOTTO_NUMBER = 1;
    private static final int MAX_LOTTO_NUMBER = 45;

    private static int getMoney() {
        System.out.println("로또 구매 금액을 입력하세요.");
        try {
            return Integer.parseInt(Console.readLine());
        } catch (NumberFormatException error) {
            throw new IllegalArgumentException("금액은 숫자로 입력해야 합니다.");
        }
    }

    private static void exception (int money) {
        if (money <= 0) {
            throw new IllegalArgumentException("금액이 0보다 커야 합니다.");
        }
        if (money % 1000 != 0) {
            throw new IllegalArgumentException("로또 금액이 1개당 1000원이므로 1000원 단위로 입력하세요.");
        }
    }

    private static int countLotto(int money) {
        return money / LOTTO_PRICE;
    }

    private static void outputCount(int count) {
        System.out.println(count + "개를 구매했습니다.");
    }

    private static List<Integer> randomNumbers() {
        List<Integer> numbers = new ArrayList<>(Randoms.pickUniqueNumbersInRange(1, 45, 6));
        numbers.sort(Integer::compareTo);
        return numbers;
    }

    private static void outputnumbers(List<Lotto> lottoList) {
        for (Lotto lotto : lottoList) {
            System.out.println(lotto.getNumbers());
        }
    }

    private static List<Integer> inputWinningNumbers() {
        System.out.println("당첨 번호를 입력해 주세요. (예: 1,2,3,4,5,6)");
        String input = Console.readLine();
        String[] splitInput = input.split(",");
        List<Integer> winningNumbers = new ArrayList<>();


        for (String num : splitInput) {
            int number = Integer.parseInt(num.trim());
            if (number < 1 || number > 45) {
                throw new IllegalArgumentException("당첨 번호는 1에서 45 사이의 숫자여야 합니다.");
            }
            if (winningNumbers.contains(number)) {
                throw new IllegalArgumentException("중복된 당첨 번호가 있습니다.");
            }
            winningNumbers.add(number);
        }
        if (winningNumbers.size() != 6) {
            throw new IllegalArgumentException("당첨 번호는 6개의 숫자를 입력해야 합니다.");
        }
        return winningNumbers;
    }

    private static int inputBonusNumber(List<Integer> winningNumbers) {
        System.out.println("보너스 번호를 입력해 주세요.");
        int bonusNumber = Integer.parseInt(Console.readLine());


        if (bonusNumber < MIN_LOTTO_NUMBER || bonusNumber > MAX_LOTTO_NUMBER) {
            throw new IllegalArgumentException("보너스 번호는 1에서 45 사이의 숫자여야 합니다.");
        }
        if (winningNumbers.contains(bonusNumber)) {
            throw new IllegalArgumentException("보너스 번호는 당첨 번호와 중복될 수 없습니다.");
        }
        return bonusNumber;
    }

    private static LottoResult calculateResult(List<Lotto> lottoList, List<Integer> winningNumbers, int bonusNumber) {
        LottoResult result = new LottoResult();

        for (Lotto lotto : lottoList) {
            int matchCount = countMatches(lotto, winningNumbers);
            if (matchCount > 0) {
                applyMatchResult(result, matchCount, isBonusMatched(lotto, bonusNumber));
            }
        }
        return result;
    }

    private static void applyMatchResult(LottoResult result, int matchCount, boolean bonusMatch) {
        result.winningResult(matchCount, bonusMatch);
    }

    private static int countMatches(Lotto lotto, List<Integer> winningNumbers) {
        int matchCount = 0;

        for (int number : lotto.getNumbers()) {
            if (winningNumbers.contains(number)) {
                matchCount++;
            }
        }

        return matchCount;
    }

    private static boolean isBonusMatched(Lotto lotto, int bonusNumber) {
        return lotto.getNumbers().contains(bonusNumber);
    }

    private static void outputResult(LottoResult result) {
        System.out.println("당첨 통계");
        System.out.println("---");
        printWinningCount("3개 일치 (5,000원)", result.getCountOf3Match());
        printWinningCount("4개 일치 (50,000원)", result.getCountOf4Match());
        printWinningCount("5개 일치 (1,500,000원)", result.getCountOf5Match());
        printWinningCount("5개 일치, 보너스 볼 일치 (30,000,000원)", result.getCountOf5MatchAndBonus());
        printWinningCount("6개 일치 (2,000,000,000원)", result.getCountOf6Match());
    }

    private static void printWinningCount(String message, int count) {
        System.out.println(message + " - " + count + "개");
    }

    private static void profitRateResult(double money, double winningPrize) {
        double profitRate = (winningPrize / money) * 100;
        profitRate = Math.round(profitRate * 100) / 100.0;

        if (profitRate % 1 == 0) {
            System.out.println("총 수익률은 " + (int) profitRate + "%입니다.");
        } else {
            System.out.println("총 수익률은 " + profitRate + "%입니다.");
        }
    }
}