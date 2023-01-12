

package com.mindorks.framework.mvvm.data.local.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.mindorks.framework.mvvm.data.model.db.Option;
import java.util.List;

import io.reactivex.Single;

@Dao
public interface OptionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Option option);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Option> options);

    @Query("SELECT * FROM options")
    Single<List<Option>> loadAll();

    @Query("SELECT * FROM options WHERE question_id = :questionId")
    Single<List<Option>> loadAllByQuestionId(Long questionId);
}
