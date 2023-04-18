package com.github.kitakkun.foos.common

import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.github.kitakkun.foos.common.navigation.ScreenRouter
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class ScreenRouterRouteTest {
    @Test
    fun testRouteWithArgs() {
        val dummyScreenRouter = object : ScreenRouter(
            route = "dummy",
            arguments = listOf(
                navArgument("arg1") { type = NavType.StringType },
                navArgument("arg2") { type = NavType.StringType },
            )
        ) {}
        assertEquals("dummy/{arg1}/{arg2}", dummyScreenRouter.routeWithArgs)
        assertEquals("dummy/arg1/arg2", dummyScreenRouter.routeWithArgs("arg1", "arg2"))
    }

    @Test
    fun testNoArgsScreenRouteWithArgs() {
        val dummyScreenRouter = object : ScreenRouter(
            route = "dummy",
        ) {}
        assertEquals("dummy", dummyScreenRouter.routeWithArgs)
        assertThrows(Exception::class.java) {
            dummyScreenRouter.routeWithArgs("arg1", "arg2")
        }
    }
}
