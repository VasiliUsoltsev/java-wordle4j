package ru.yandex.practicum;

import java.util.Objects;

//  Класс для хранения правила загаданного слова
public class RuleSecretWord {
    private final char symbol;
    private final char sign;
    private final int sequenceNumber;

    public char getSymbol() {
        return symbol;
    }

    public char getSign() {
        return sign;
    }

    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    public RuleSecretWord(char symbol, char sign, int sequenceNumber) {
        this.symbol = symbol;
        this.sign = sign;
        this.sequenceNumber = sequenceNumber;
    }

    public RuleSecretWord(char symbol, char sign) {
        this.symbol = symbol;
        this.sign = sign;
        this.sequenceNumber = -1;
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol, sign, sequenceNumber);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null && this.getClass() != obj.getClass()) return false;
        if (this == obj) return true;

        RuleSecretWord o = (RuleSecretWord) obj;
        return  this.symbol == o.symbol &&
                this.sign == o.sign &&
                this.sequenceNumber == o.sequenceNumber;
    }

    @Override
    public String toString() {
            return "\n {символ = " + symbol + ", знак = " + sign + ", порядковый номер = " + sequenceNumber + "}";
    }
}
