package com.ingco.shopify.api

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class UtilsTest {

    @ParameterizedTest
    @CsvSource(
        "10-броя-hss-свредло-ingco-dbt1100303, dbt1100303",
        "led-крушка-топла-светлина-ingco-hlbacd252-5w, hlbacd252",
        "sds-max-длето-секач-ingco-dbc0222801-30см, dbc0222801",
        "диамантен-диск-dry-ingco-dmd012302m-230мм, dmd012302m",
        "гумен-чук-с-дръжка-от-вибростъкло-ingco-hruh8308-220гр, hruh8308",
        "акумулаторна-бормашина-винтоверт-ingco-cdli1232-12v, cdli1232",
        "електрическа-бъркалка-миксер-ingco-mx214008-1400w, mx214008",
        "електрическа-косачка-ingco-lm383-1600w, lm383",
        "инвенторен-електрожен-мма-ingco-ing-mma2006, ing-mma2006",
        "инвенторен-електрожен-мма-ingco-ing-mmac1602, ing-mmac1602",
        "комплект-двустранни-накрайници-битове-за-отвертка-10-броя-ingco-sdb21hl133-ph2-sl6-0-65мм, sdb21hl133",
        "комплект-двустранни-накрайници-битове-за-отвертка-10-броя-ingco-sdb21ph223-ph2-50мм, sdb21ph223",
        "комплект-накрайници-битове-за-отвертка-10-броя-ingco-sdb11sl423-sl6-0-1-0-50мм, sdb11sl423",
        "комплект-накрайници-битове-за-отвертка-20-броя-ingco-sdb11ph213-ph2-25мм, sdb11ph213",
        "потопяема-помпа-с-поплавък-ingco-spd7501-750w-13000л-час, spd7501",
        "поялник-за-полипропиленови-тръби-ingco-ptwt215002-800w-1500w, ptwt215002",
        "комплект-звездогаечни-ключове-8-броя-ingco-hkspa1088-i, hkspa1088-i",
        "ексцентър-шлайф-ingco-rs4501-2-450w, rs4501.2",
        "комплект-ударни-вложки-10-броя-ingco-hkissd12101-1-2, hkissd12101",
        "литиево-йонна-батерия-ingco-fbli12151-1-5ah, fbli12151",
        "работни-ръкавици-ingco-hgcg01-xl, hgcg01-xl",
        "ротационен-перфоратор-ingco-rgh9018-2, rgh9018-2",
        "ротационен-перфоратор-ingco-rgh9028-800w, rgh9028",
        "ротационен-перфоратор-ingco-rgh9028-2-800w, rgh9028-2",
        "ъглошлайф-ingco-ag8006-2, ag8006-2",
        "ротационен-перфоратор-sds-max-ingco-rh16008, rh16008",
        "зарядно-устройство-ingco-ing-cb1601, ing-cb1601"
    )
    fun `extracts product code from handle`(handle: String, expectedProductCode: String) {
        assertThat(productCode(handle)).isEqualTo(expectedProductCode)
    }
}