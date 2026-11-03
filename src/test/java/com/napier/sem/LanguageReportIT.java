package com.napier.sem;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LanguageReportIT extends IntegrationTestBase
{
    @Test
    void returnsFiveLanguages()
    {
        var statistics = app.getLanguageStatistics();

        assertNotNull(statistics);
        assertEquals(5, statistics.size());
    }

    @Test
    void chineseLanguageStatisticsAreCorrect()
    {
        var statistics = app.getLanguageStatistics();

        LanguageStatistic chinese = statistics.get(0);

        assertEquals("Chinese", chinese.language);
        assertEquals(1191843539L, chinese.speakers);
        assertEquals(19.61, chinese.worldPercentage, 0.01);
    }

    @Test
    void hindiLanguageStatisticsAreCorrect()
    {
        var statistics = app.getLanguageStatistics();

        LanguageStatistic hindi = statistics.get(1);

        assertEquals("Hindi", hindi.language);
        assertEquals(405633070L, hindi.speakers);
        assertEquals(6.67, hindi.worldPercentage, 0.01);
    }

    @Test
    void spanishLanguageStatisticsAreCorrect()
    {
        var statistics = app.getLanguageStatistics();

        LanguageStatistic spanish = statistics.get(2);

        assertEquals("Spanish", spanish.language);
        assertEquals(355029462L, spanish.speakers);
        assertEquals(5.84, spanish.worldPercentage, 0.01);
    }

    @Test
    void englishLanguageStatisticsAreCorrect()
    {
        var statistics = app.getLanguageStatistics();

        LanguageStatistic english = statistics.get(3);

        assertEquals("English", english.language);
        assertEquals(347077867L, english.speakers);
        assertEquals(5.71, english.worldPercentage, 0.01);
    }

    @Test
    void arabicLanguageStatisticsAreCorrect()
    {
        var statistics = app.getLanguageStatistics();

        LanguageStatistic arabic = statistics.get(4);

        assertEquals("Arabic", arabic.language);
        assertEquals(233839239L, arabic.speakers);
        assertEquals(3.85, arabic.worldPercentage, 0.01);
    }
}