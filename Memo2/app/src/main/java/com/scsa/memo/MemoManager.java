package com.scsa.memo;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MemoManager {
    private static final String TAG = "MemoManager";
    private static MemoManager instance;
    private List<MemoDto> list;
    private MemoManager() {
        list = new ArrayList<>();
    }

    public static MemoManager getInstance() {
        if (instance == null) {
            instance = new MemoManager();
        }
        return instance;
    }

    /**
     * 데이터 전체를 반환한다.
     * @return
     */
    public List<MemoDto> getList() {
        return list;
    }

    /**
     * 위치에 해당하는 데이터를 저장소에서 찾아 반환한다.
     * @param index 위치
     * @return MemoDto, 찾는 index가 없는 경우 null
     */
    public MemoDto get(int index) {
        if (index < list.size()) {
            return list.get(index);
        }
        return null;
    }

    /**
     * 데이터를 저장소에 추가한다.
     * @param dto
     */
    public void add (MemoDto dto) {
        Log.i(TAG, "before: " + list.size());
        list.add(dto);
        Log.i(TAG, "add: " + dto.getTitle());
        Log.i(TAG, "after: " + list.size());
    }

    /**
     * 위치에 해당하는 데이터를 새로운 데이터로 교체한다.
     * 찾는 index가 없는 경우 무시한다.
     * @param index 위치
     * @return dto 새로운 데이터
     */
    public void update(int index, MemoDto dto) {
        if (index < list.size()) {
            list.set(index, dto);
        }
    }

    /**
     * 위치에 해당하는 데이터를 삭제한다.
     * 찾는 index가 없는 경우 무시한다.
     * @param index
     */
    public void remove(int index) {
        if (index < list.size()){
            list.remove(index);
        }
    }
}

