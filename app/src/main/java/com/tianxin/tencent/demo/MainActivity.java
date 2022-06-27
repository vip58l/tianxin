package com.tianxin.tencent.demo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tianxin.R;
import com.tianxin.tencent.demo.common.widget.expandableadapter.BaseExpandableRecyclerViewAdapter;
import com.tencent.rtmp.TXLiveBase;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 展示主界面相关内容
 */
public class MainActivity extends Activity {
    private static final String TAG = MainActivity.class.getName();
    private TextView mMainTitle, mTvVersion;
    private RecyclerView mRvList;
    private MainExpandableAdapter mAdapter;
    private ImageView mLogoutImg;
    private AlertDialog mAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int is = (getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        if (is != 0) {
            Log.d(TAG, "" + is);
            finish();
            return;
        }
        setContentView(R.layout.activity_main8);
        myonCreate();
    }

    private void myonCreate() {
        mTvVersion = findViewById(R.id.main_tv_version);
        mTvVersion.setText("视频云工具包 v" + TXLiveBase.getSDKVersionStr() + "(8.2.639)");
        mMainTitle = findViewById(R.id.main_title);
        mMainTitle.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        File logFile = getLogFile();
                        Log.d(TAG, logFile.getName() + logFile.toString());
                        if (logFile != null) {
                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.setType("application/octet-stream");
                            //intent.setPackage("com.tencent.mobileqq");
                            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(logFile));
                            startActivity(Intent.createChooser(intent, "分享日志"));
                        }
                    }
                });
                return false;
            }
        });
        mLogoutImg = findViewById(R.id.img_logout);
        mLogoutImg.setVisibility(View.VISIBLE);
        mLogoutImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutDialog();
            }
        });
        mRvList = findViewById(R.id.main_recycler_view);
        List<GroupBean> groupBeans = initGroupData();
        mRvList.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MainExpandableAdapter(groupBeans);
        mAdapter.setListener(new BaseExpandableRecyclerViewAdapter.ExpandableRecyclerViewOnClickListener<GroupBean, ChildBean>() {
            @Override
            public boolean onGroupLongClicked(GroupBean groupItem) {
                return false;
            }

            @Override
            public boolean onInterceptGroupExpandEvent(GroupBean groupItem, boolean isExpand) {
                return false;
            }

            @Override
            public void onGroupClicked(GroupBean groupItem) {
                mAdapter.setSelectedChildBean(groupItem);
            }

            @Override
            public void onChildClicked(GroupBean groupItem, ChildBean childItem) {

                if (childItem.mIconId == R.drawable.xiaoshipin) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("http://dldir1.qq.com/hudongzhibo/xiaozhibo/XiaoShiPin.apk"));
                    startActivity(intent);
                    return;
                } else if (childItem.mIconId == R.drawable.xiaozhibo) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("http://dldir1.qq.com/hudongzhibo/xiaozhibo/xiaozhibo.apk"));
                    startActivity(intent);
                    return;
                }

                Intent intent = new Intent(MainActivity.this, childItem.getTargetClass());
                intent.putExtra("TITLE", childItem.mName);
                intent.putExtra("TYPE", childItem.mType);
                startActivity(intent);
            }
        });
        mRvList.setAdapter(mAdapter);
    }

    //确定要退出登录吗
    private void showLogoutDialog() {
        if (mAlertDialog == null) {
            mAlertDialog = new AlertDialog.Builder(this, R.style.common_alert_dialog)
                    .setMessage("确定要退出登录吗？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            /*// 执行退出登录操作
                            ProfileManager.getInstance().logout(new ProfileManager.ActionCallback() {
                                @Override
                                public void onSuccess() {
                                    //停止通信后台服务
                                    CallService.stop(MainActivity.this);
                                    // 退出登录
                                    startLoginActivity();
                                }

                                @Override
                                public void onFailed(int code, String msg) {
                                }
                            });
                        */}
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create();
        }
        if (!mAlertDialog.isShowing()) {
            mAlertDialog.show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //登录成功启动服务
        CallService.start(this);
    }

    @Override
    public void onBackPressed() {
        //showLogoutDialog();
        finish();
    }

    /**
     * 退出后的主页
     */
    private void startLoginActivity() {
        //Intent intent = new Intent(this, LoginActivity.class);
        //Intent intent = new Intent(this, UserLoginActivity.class);
        //startActivity(intent);
        //finish();
    }

    /**
     * 视频通话列表
     *
     * @return
     */
    private List<GroupBean> initGroupData() {
        List<GroupBean> groupList = new ArrayList<>();
        List<ChildBean> videoConnectChildList = new ArrayList<>();
        //videoConnectChildList.add(new ChildBean("多人视频会议", R.drawable.multi_meeting, 0, CreateMeetingActivity.class));
        //videoConnectChildList.add(new ChildBean("语音互动聊天", R.drawable.room_multi, 0, VoiceRoomListActivity.class));
        //videoConnectChildList.add(new ChildBean("视频互动直播", R.drawable.room_multi, 1, LiveRoomListActivity.class));
        //videoConnectChildList.add(new ChildBean("语音通话", R.drawable.room_multi, TRTCCalling.TYPE_AUDIO_CALL, TRTCCallingEntranceActivity.class));
       //videoConnectChildList.add(new ChildBean("视频通话", R.drawable.room_multi, TRTCCalling.TYPE_VIDEO_CALL, TRTCCallingEntranceActivity.class));

        if (videoConnectChildList.size() != 0) {
            GroupBean videoConnectGroupBean = new GroupBean("实时音视频 TRTC", R.drawable.room_multi, videoConnectChildList);
            groupList.add(videoConnectGroupBean);
        }
        return groupList;
    }

    /**
     * 适配器
     */
    private static class MainExpandableAdapter extends BaseExpandableRecyclerViewAdapter<GroupBean, ChildBean, GroupVH, ChildVH> {
        private final List<GroupBean> mListGroupBean;
        private GroupBean mGroupBean;

        public void setSelectedChildBean(GroupBean groupBean) {
            boolean isExpand = isExpand(groupBean);
            if (mGroupBean != null) {
                GroupVH lastVH = getGroupViewHolder(mGroupBean);
                if (!isExpand)
                    mGroupBean = groupBean;
                else
                    mGroupBean = null;
                notifyItemChanged(lastVH.getAdapterPosition());
            } else {
                if (!isExpand)
                    mGroupBean = groupBean;
                else
                    mGroupBean = null;
            }
            if (mGroupBean != null) {
                GroupVH currentVH = getGroupViewHolder(mGroupBean);
                notifyItemChanged(currentVH.getAdapterPosition());
            }
        }

        public MainExpandableAdapter(List<GroupBean> list) {
            mListGroupBean = list;
        }

        @Override
        public int getGroupCount() {
            return mListGroupBean.size();
        }

        @Override
        public GroupBean getGroupItem(int groupIndex) {
            return mListGroupBean.get(groupIndex);
        }

        @Override
        public GroupVH onCreateGroupViewHolder(ViewGroup parent, int groupViewType) {
            return new GroupVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.module_entry_item, parent, false));
        }

        @Override
        public void onBindGroupViewHolder(GroupVH holder, GroupBean groupBean, boolean isExpand) {
            holder.textView.setText(groupBean.mName);
            holder.ivLogo.setImageResource(groupBean.mIconId);
            holder.itemView.setBackgroundResource(mGroupBean == groupBean ? R.color.beauty_color_red : R.color.rtc_green_bg);
        }

        @Override
        public ChildVH onCreateChildViewHolder(ViewGroup parent, int childViewType) {
            return new ChildVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.module_entry_child_item, parent, false));
        }

        @Override
        public void onBindChildViewHolder(ChildVH holder, GroupBean groupBean, ChildBean childBean) {
            holder.textView.setText(childBean.getName());

            if (groupBean.mChildList.indexOf(childBean) == groupBean.mChildList.size() - 1) {
                //说明是最后一个
                holder.divideView.setVisibility(View.GONE);
            } else {
                holder.divideView.setVisibility(View.VISIBLE);
            }

        }
    }

    /**
     * 内容组件子类1
     */
    public static class GroupVH extends BaseExpandableRecyclerViewAdapter.BaseGroupViewHolder {
        ImageView ivLogo;
        TextView textView;

        GroupVH(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.name_tv);
            ivLogo = itemView.findViewById(R.id.icon_iv);
        }

        @Override
        protected void onExpandStatusChanged(RecyclerView.Adapter relatedAdapter, boolean isExpanding) {
        }

    }

    /**
     * 内容组件子类2
     */
    public static class ChildVH extends RecyclerView.ViewHolder {
        TextView textView;
        View divideView;

        ChildVH(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.name_tv);
            divideView = itemView.findViewById(R.id.item_view_divide);
        }

    }

    /**
     * 导航分类内容
     */
    private class GroupBean implements BaseExpandableRecyclerViewAdapter.BaseGroupBean<ChildBean> {
        private final String mName;
        private final List<ChildBean> mChildList;
        private final int mIconId;

        public GroupBean(String name, int iconId, List<ChildBean> list) {
            mName = name;
            mChildList = list;
            mIconId = iconId;
        }

        @Override
        public int getChildCount() {
            return mChildList.size();
        }

        @Override
        public ChildBean getChildAt(int index) {
            return mChildList.size() <= index ? null : mChildList.get(index);
        }

        @Override
        public boolean isExpandable() {
            return getChildCount() > 0;
        }

        public String getName() {
            return mName;
        }

        public List<ChildBean> getChildList() {
            return mChildList;
        }

        public int getIconId() {
            return mIconId;
        }
    }

    /**
     * 返回打开指定类
     */
    private class ChildBean {
        public String mName;
        public int mIconId;
        public Class mTargetClass;
        public int mType;

        public ChildBean(String name, int iconId, int type, Class targetActivityClass) {
            this.mName = name;
            this.mIconId = iconId;
            this.mTargetClass = targetActivityClass;
            this.mType = type;
        }

        public String getName() {
            return mName;
        }


        public int getIconId() {
            return mIconId;
        }


        public Class getTargetClass() {
            return mTargetClass;
        }
    }

    private File getLogFile() {
        File sdcardDir = getExternalFilesDir(null);
        if (sdcardDir == null) {
            return null;
        }

        String path = sdcardDir.getAbsolutePath() + "/log/tencent/liteav";
        List<String> logs = new ArrayList<>();
        File directory = new File(path);
        if (directory != null && directory.exists() && directory.isDirectory()) {
            long lastModify = 0;
            File[] files = directory.listFiles();
            if (files != null && files.length > 0) {
                for (File file : files) {
                    if (file.getName().endsWith("xlog")) {
                        logs.add(file.getAbsolutePath());
                    }
                }
            }
        }


        String zipPath = path + "/liteavLog.zip";
        return zip(logs, zipPath);
    }

    private File zip(List<String> files, String zipFileName) {
        File zipFile = new File(zipFileName);
        zipFile.deleteOnExit();
        InputStream is = null;
        ZipOutputStream zos = null;

        try {
            zos = new ZipOutputStream(new FileOutputStream(zipFile));
            zos.setComment("LiteAV log");
            for (String path : files) {
                File file = new File(path);
                try {
                    if (file.length() == 0 || file.length() > 8 * 1024 * 1024) continue;

                    is = new FileInputStream(file);
                    zos.putNextEntry(new ZipEntry(file.getName()));
                    byte[] buffer = new byte[8 * 1024];
                    int length = 0;
                    while ((length = is.read(buffer)) != -1) {
                        zos.write(buffer, 0, length);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        is.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (FileNotFoundException e) {
            Log.w(TAG, "zip log error");
            zipFile = null;
        } finally {
            try {
                zos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return zipFile;
    }
}
