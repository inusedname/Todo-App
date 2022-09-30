package com.vstd.todo

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

/**
1. Thêm Task
2. Thêm Task có ngày
3. Thêm Task có ngày có giờ
4. Thêm Workspace
5. Ghi đè workspace
6. Sửa task: Không sửa: Ấn save, back, back hệ thống
7. Sửa task: Sửa hết: Ấn save....
8. Sửa task: Đổi workspace: ...
8. Sửa task + Done
9. Done
10. Sửa task + Archive
11. Archive
12. Xoá hẳn khỏi archive
13. Restore from archive
13. Mở archive, restore, quay lại all
14. Mở archive, quay lại all, archive, rồi quay lại archive
 */
/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.vstd.todo", appContext.packageName)
    }
}