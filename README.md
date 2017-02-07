#RxUtilDemo
 //使用内部封装的线程池进行耗时任务
 
        Toast.makeText(this, "IO线程进行耗时操作,请在控制台查看Log", Toast.LENGTH_SHORT).show();
        String str = "模拟IO线程执行耗时操作";
        RxjavaUtil.doInIOThread(new IOTask<String>(str) {
            @Override
            public void doInIOThread() {
                for (int i = 0; i < 5; i++) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.v("test", getT());
                }
            }
        });
        
 //在子线程中修改UI
 
        new Thread(new Runnable() {
            @Override
            public void run() {
                String str2 = "子线程中修改UI";
                RxjavaUtil.doInUIThread(new UITask<String>(str2) {
                    @Override
                    public void doInUIThread() {
                        tv_test.setText(getT());
                    }
                });
            }
        }).start();
        
  //在IO线程进行耗时操作 执行完成后修改UI
  
        String str3 = "耗时操作结束修改UI";
        Toast.makeText(this, "请等候三秒再看测试的文本框内容变化", Toast.LENGTH_SHORT).show();
        RxjavaUtil.executeRxTask(new CommonRxTask<String>(str3) {
            @Override
            public void doInIOThread() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                setT(getT() + "--------小尾巴");
            }

            @Override
            public void doInUIThread() {
                tv_test.setText(getT());
            }
        });
