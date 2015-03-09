package com.pushtorefresh.android.bamboostorage.unit_test.operation;

import android.database.Cursor;
import android.support.annotation.NonNull;

import com.pushtorefresh.android.bamboostorage.BambooStorage;
import com.pushtorefresh.android.bamboostorage.operation.MapFunc;
import com.pushtorefresh.android.bamboostorage.operation.get.PreparedGet;
import com.pushtorefresh.android.bamboostorage.query.Query;
import com.pushtorefresh.android.bamboostorage.query.RawQuery;
import com.pushtorefresh.android.bamboostorage.unit_test.design.User;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PreparedGetTest {

    private static class GetStub {
        final BambooStorage bambooStorage;
        final MapFunc<Cursor, User> mapFunc;
        final Query query;
        final RawQuery rawQuery;
        final BambooStorage.Internal internal;

        GetStub() {
            bambooStorage = mock(BambooStorage.class);
            query = mock(Query.class);
            rawQuery = mock(RawQuery.class);
            internal = mockInternal();

            when(bambooStorage.get())
                    .thenReturn(new PreparedGet.Builder(bambooStorage));

            when(bambooStorage.internal())
                    .thenReturn(internal);

            //noinspection unchecked
            mapFunc = (MapFunc<Cursor, User>) mock(MapFunc.class);

            when(mapFunc.map(any(Cursor.class)))
                    .thenReturn(mock(User.class));
        }

        private BambooStorage.Internal mockInternal() {
            final int mockObjectsSize = 3;
            final Cursor cursorStub = mock(Cursor.class);
            when(cursorStub.moveToNext()).thenAnswer(new Answer<Boolean>() {
                int invocationsCount = 0;

                @Override public Boolean answer(InvocationOnMock invocation) throws Throwable {
                    return invocationsCount++ < mockObjectsSize;
                }
            });

            BambooStorage.Internal internal = mock(BambooStorage.Internal.class);
            when(internal.query(query)).thenReturn(cursorStub);
            when(internal.rawQuery(rawQuery)).thenReturn(cursorStub);
            return internal;
        }
    }

    private void verifyQueryBehavior(@NonNull final GetStub getStub) {
        verify(getStub.bambooStorage, times(1)).get();
        verify(getStub.internal, times(1)).query(any(Query.class));
    }

    private void verifyQueryBehaviorForList(@NonNull final GetStub getStub) {
        verify(getStub.bambooStorage, times(1)).get();
        verify(getStub.mapFunc, times(3)).map(any(Cursor.class));
        verify(getStub.internal, times(1)).query(any(Query.class));
    }

    @Test public void getCursorBlocking() {
        final GetStub getStub = new GetStub();

        getStub.bambooStorage
                .get()
                .cursor()
                .withQuery(getStub.query)
                .prepare()
                .executeAsBlocking();

        verifyQueryBehavior(getStub);
    }

    @Test public void getListOfObjectsBlocking() {
        final GetStub getStub = new GetStub();

        getStub.bambooStorage
                .get()
                .listOfObjects(User.class)
                .withMapFunc(getStub.mapFunc)
                .withQuery(getStub.query)
                .prepare()
                .executeAsBlocking();

        verifyQueryBehaviorForList(getStub);
    }

    @Test public void getCursorObservable() {
        final GetStub getStub = new GetStub();

        getStub.bambooStorage
                .get()
                .cursor()
                .withQuery(getStub.query)
                .prepare()
                .createObservable()
                .toBlocking()
                .last();

        verifyQueryBehavior(getStub);
    }


    @Test public void getListOfObjectsObservable() {
        final GetStub getStub = new GetStub();

        getStub.bambooStorage
                .get()
                .listOfObjects(User.class)
                .withMapFunc(getStub.mapFunc)
                .withQuery(getStub.query)
                .prepare()
                .createObservable()
                .toBlocking()
                .last();

        verifyQueryBehaviorForList(getStub);
    }

    private void verifyRawQueryBehavior(@NonNull final GetStub getStub) {
        verify(getStub.bambooStorage, times(1)).get();
        verify(getStub.internal, times(1)).rawQuery(any(RawQuery.class));
    }

    private void verifyRawQueryBehaviorForList(@NonNull final GetStub getStub) {
        verify(getStub.bambooStorage, times(1)).get();
        verify(getStub.mapFunc, times(3)).map(any(Cursor.class));
        verify(getStub.internal, times(1)).rawQuery(any(RawQuery.class));
    }

    @Test public void getCursorWithRawQueryBlocking() {
        final GetStub getStub = new GetStub();

        getStub.bambooStorage
                .get()
                .cursor()
                .withQuery(getStub.rawQuery)
                .prepare()
                .executeAsBlocking();

        verifyRawQueryBehavior(getStub);
    }

    @Test public void getCursorWithRawQueryObservable() {
        final GetStub getStub = new GetStub();

        getStub.bambooStorage
                .get()
                .cursor()
                .withQuery(getStub.rawQuery)
                .prepare()
                .createObservable()
                .toBlocking()
                .last();

        verifyRawQueryBehavior(getStub);
    }

    @Test public void getListOfObjectsWithRawQueryBlocking() {
        final GetStub getStub = new GetStub();

        getStub.bambooStorage
                .get()
                .listOfObjects(User.class)
                .withMapFunc(getStub.mapFunc)
                .withQuery(getStub.rawQuery)
                .prepare()
                .executeAsBlocking();

        verifyRawQueryBehaviorForList(getStub);
    }

    @Test public void getListOfObjectsWithRawQueryObservable() {
        final GetStub getStub = new GetStub();

        getStub.bambooStorage
                .get()
                .listOfObjects(User.class)
                .withMapFunc(getStub.mapFunc)
                .withQuery(getStub.rawQuery)
                .prepare()
                .createObservable()
                .toBlocking()
                .last();

        verifyRawQueryBehaviorForList(getStub);
    }
}
