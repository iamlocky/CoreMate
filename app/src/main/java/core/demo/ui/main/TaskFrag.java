package core.demo.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;

import core.demo.R;
import core.demo.task.DeleteFileTask;
import core.demo.task.ExportAssetsTask;
import core.demo.task.ReadTextTask;
import core.mate.app.CoreFrag;
import core.mate.app.ProgressDlgFrag;
import core.mate.async.OnTaskListenerImpl;
import core.mate.util.ToastUtil;

/**
 * 封装的异步任务的演示。
 * <p>
 * 一个类只做一件事情，所以这里建议将每一个异步任务单独封装成一个类，
 * 并让业务逻辑通过回调来处理任务结果。
 */
public class TaskFrag extends CoreFrag {

    /*继承*/

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_task, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.button_frag_task_export).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exportAssets();
            }
        });
        view.findViewById(R.id.button_frag_task_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteExportedFile();
            }
        });
        view.findViewById(R.id.button_frag_task_read).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readExportedFile();
            }
        });
    }

    /*拓展*/

    private static final String ASSETS = "test.txt";

    private void exportAssets() {
        new ExportAssetsTask()
                //添加不可取消的转菊花对话框，阻塞用户操作
                .addIndicator(new ProgressDlgFrag().setFragmentManager(this))
                //添加回调
                .addOnTaskListener(new OnTaskListenerImpl<File>() {
                    @Override
                    public void onSuccess(File file) {
                        ToastUtil.toastShort("成功导出到=" + file.getAbsolutePath());
                    }
                })
                //将assets中的文件导出到/data/data/包名/files目录下
                .execute(new ExportAssetsTask.Params(ASSETS, getContext().getFilesDir()));
    }

    private void deleteExportedFile() {
        new DeleteFileTask()
                //添加不可取消的转菊花对话框，阻塞用户操作
                .addIndicator(new ProgressDlgFrag().setFragmentManager(this))
                //添加回调
                .addOnTaskListener(new OnTaskListenerImpl<boolean[]>() {

                    @Override
                    public void onSuccess(boolean[] booleen) {
                        ToastUtil.toastShort("执行删除完毕");
                    }
                })
                //删除导出的文件
                .execute(new File[]{new File(getContext().getFilesDir(), ASSETS)});
    }

    private void readExportedFile() {
        new ReadTextTask()
                //添加不可取消的转菊花对话框，阻塞用户操作
                .addIndicator(new ProgressDlgFrag().setFragmentManager(this))
                //添加回调
                .addOnTaskListener(new OnTaskListenerImpl<String>() {

                    @Override
                    public void onFailure(Exception e) {
                        super.onFailure(e);
                        ToastUtil.toastShort("读取文件失败，请先导出文件");
                    }

                    @Override
                    public void onSuccess(String s) {
                        ToastUtil.toastShort("读取内容 = " + s);
                    }
                })
                .execute(new File(getContext().getFilesDir(), ASSETS));
    }
}
