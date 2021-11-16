import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.RecyclerView


class ItemOffsetDecoration(private val mItemOffset: Int) : RecyclerView.ItemDecoration() {
    constructor(context: Context, @DimenRes itemOffsetId: Int) : this(
        context.getResources().getDimensionPixelSize(itemOffsetId)
    ) {
    }
}