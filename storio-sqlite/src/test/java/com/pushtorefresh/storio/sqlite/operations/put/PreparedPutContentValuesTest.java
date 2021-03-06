package com.pushtorefresh.storio.sqlite.operations.put;

import org.junit.Test;

import rx.Completable;
import rx.Observable;
import rx.Single;

public class PreparedPutContentValuesTest {

    @Test
    public void putContentValuesBlocking() {
        final PutContentValuesStub putStub = PutContentValuesStub.newPutStubForOneContentValues();

        final PutResult putResult = putStub.storIOSQLite
                .put()
                .contentValues(putStub.contentValues.get(0))
                .withPutResolver(putStub.putResolver)
                .prepare()
                .executeAsBlocking();

        putStub.verifyBehaviorForOneContentValues(putResult);
    }

    @Test
    public void putContentValuesObservable() {
        final PutContentValuesStub putStub = PutContentValuesStub.newPutStubForOneContentValues();

        final Observable<PutResult> putResultObservable = putStub.storIOSQLite
                .put()
                .contentValues(putStub.contentValues.get(0))
                .withPutResolver(putStub.putResolver)
                .prepare()
                .asRxObservable();

        putStub.verifyBehaviorForOneContentValues(putResultObservable);
    }

    @Test
    public void putContentValuesSingle() {
        final PutContentValuesStub putStub = PutContentValuesStub.newPutStubForOneContentValues();

        final Single<PutResult> putResultSingle = putStub.storIOSQLite
                .put()
                .contentValues(putStub.contentValues.get(0))
                .withPutResolver(putStub.putResolver)
                .prepare()
                .asRxSingle();

        putStub.verifyBehaviorForOneContentValues(putResultSingle);
    }

    @Test
    public void putContentValuesCompletable() {
        final PutContentValuesStub putStub = PutContentValuesStub.newPutStubForOneContentValues();

        final Completable completable = putStub.storIOSQLite
                .put()
                .contentValues(putStub.contentValues.get(0))
                .withPutResolver(putStub.putResolver)
                .prepare()
                .asRxCompletable();

        putStub.verifyBehaviorForOneContentValues(completable);
    }
}
