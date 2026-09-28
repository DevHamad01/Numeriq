package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.SolvedProblem
import kotlinx.coroutines.flow.Flow

@Dao
interface ProblemDao {
    @Query("SELECT * FROM solved_problems ORDER BY timestamp DESC")
    fun getAllProblems(): Flow<List<SolvedProblem>>

    @Query("SELECT * FROM solved_problems ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentProblems(limit: Int): Flow<List<SolvedProblem>>

    @Query("SELECT * FROM solved_problems WHERE id = :id")
    suspend fun getProblemById(id: Long): SolvedProblem?

    @Query("SELECT * FROM solved_problems WHERE question LIKE '%' || :query || '%' OR finalAnswer LIKE '%' || :query || '%' ORDER BY timestamp DESC")
    fun searchProblems(query: String): Flow<List<SolvedProblem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProblem(problem: SolvedProblem): Long

    @Update
    suspend fun updateProblem(problem: SolvedProblem)

    @Delete
    suspend fun deleteProblem(problem: SolvedProblem)

    @Delete
    suspend fun deleteProblems(problems: List<SolvedProblem>)

    @Query("DELETE FROM solved_problems WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM solved_problems")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM solved_problems")
    fun getCount(): Flow<Int>
}
