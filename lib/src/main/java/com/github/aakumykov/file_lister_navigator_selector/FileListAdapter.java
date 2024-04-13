package com.github.aakumykov.file_lister_navigator_selector;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import com.github.aakumykov.file_lister_navigator_selector.fs_item.FSItem;
import com.github.aakumykov.file_lister_navigator_selector.fs_item.ParentDirItem;
import com.github.aakumykov.file_lister_navigator_selector.sorting_info_supplier.SimpleSortingInfoSupplier;
import com.github.aakumykov.file_lister_navigator_selector.sorting_info_supplier.SortingInfoSupplier;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

// FIXME: зависимость от внешнего класса FSItem вызывает у меня сомнения (инкапсуляция)
public class FileListAdapter<SortingModeType> extends ArrayAdapter<FSItem> {

    public static String TAG = FileListAdapter.class.getSimpleName();

    private final static String defaultFolderGraphicalCharacter = "\uD83D\uDCC1";
    private final static String defaultFileGraphicalCharacter = "\uD83D\uDCC4";

    private final Context context;
    private final LayoutInflater inflater;
    private final int layout;
    private final int titleViewId;
    private final int infoViewId;
    private final List<FSItem> selectionsList = new ArrayList<>();
    private final String folderGraphicalCharacter;
    private final String fileGraphicalCharacter;
    private final SortingInfoSupplier<SortingModeType> sortingInfoSupplier;

    private SortingModeType sortingMode;

    public FileListAdapter(Context context,
                           @LayoutRes int resource,
                           @IdRes int titleViewId,
                           @IdRes int infoViewId,
                           SortingInfoSupplier<SortingModeType> sortingInfoSupplier,
                           SortingModeType initialSortingMode
    ) {
        this(context,
            resource,
            titleViewId,
            infoViewId,
            defaultFolderGraphicalCharacter,
            defaultFileGraphicalCharacter,
            sortingInfoSupplier,
            initialSortingMode);
    }

    public FileListAdapter(Context context,
                           @LayoutRes int resource,
                           @IdRes int titleViewId,
                           @IdRes int infoViewId,
                           String folderGraphicalCharacter,
                           String fileGraphicalCharacter,
                           SortingInfoSupplier<SortingModeType> sortingInfoSupplier,
                           SortingModeType initialSortingMode
    ) {
        super(context, resource, new ArrayList<>());
        this.context = context;
        this.layout = resource;
        this.titleViewId = titleViewId;
        this.infoViewId = infoViewId;
        this.inflater = LayoutInflater.from(context);
        this.folderGraphicalCharacter = folderGraphicalCharacter;
        this.fileGraphicalCharacter = fileGraphicalCharacter;
        this.sortingInfoSupplier = sortingInfoSupplier;
        this.sortingMode = initialSortingMode;
    }


    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        ViewHolder viewHolder;
        if(convertView==null){
            convertView = inflater.inflate(this.layout, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        FSItem fsItem = super.getItem(position);

        if (null == fsItem)
            throw new IllegalStateException("FSItem is null");

        String title;
        if (fsItem instanceof ParentDirItem)
            title = " " + fsItem.getName();
        else
            title = (fsItem.isDir()) ?
                folderGraphicalCharacter + " " + fsItem.getName()
                : fileGraphicalCharacter + " " + fsItem.getName();

        String fileInfo = "";
        if (!fsItem.isDir())
            fileInfo = sortingInfoSupplier.getSortingInfo(context, fsItem, sortingMode, " (", ")");

        if (selectionsList.contains(fsItem))
            title = "*" + " " + title;

        viewHolder.nameView.setText(title);
        viewHolder.infoView.setText(fileInfo);

        return convertView;
    }

    public void setList(List<FSItem> list) {
        super.clear();
        super.addAll(list);
        notifyDataSetChanged();
    }

    public void updateSelections(@NotNull List<FSItem> list) {
        selectionsList.clear();
        selectionsList.addAll(list);
        notifyDataSetChanged();
    }

    public void changeSortingMode(SortingModeType sortingMode) {
        this.sortingMode = sortingMode;
        notifyDataSetChanged();
    }

    private class ViewHolder {
        final TextView nameView;
        final TextView infoView;

        ViewHolder(View view){
            nameView = view.findViewById(titleViewId);
            infoView = view.findViewById(infoViewId);
        }
    }
}