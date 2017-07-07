/*
 * Copyright 2015 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package task.application.com.moviefinder.navigation;

/**
 * A Presenter acts as a controller for both the {@link com.google.samples.apps.iosched.archframework.UpdatableView} (typically a fragment) and
 * the {@link Model} for the MVP architectural pattern.
 * <p/>
 * It is parametrised by the {@link QueryEnum} (the list of queries it is able to pass on to the
 * Model) and the {@link UserActionEnum} (the list of user actions it is able to pass on to the
 * Model).
 * <p/>
 * Typically, upon being created, a Presenter requests the Model to load the initial data, then
 * update the View accordingly.
 * <p/>
 * The implementation of a Presenter listens to events generated by the {@link com.google.samples.apps.iosched.archframework.UpdatableView} (by
 * implementing {@link com.google.samples.apps.iosched.archframework.UpdatableView
 * .UserActionListener}, and passes them on to the {@link Model}, which modifies its data as
 * required. The Presenter also listens to events generated by the {@link Model} (by using {@link
 * com.google.samples.apps.iosched .archframework.Model.DataQueryCallback} and {@link
 * com.google.samples.apps.iosched .archframework.Model.UserActionCallback} and then asks the {@link
 * com.google.samples.apps.iosched.archframework.UpdatableView} to update itself.
 */
public interface Presenter<Q extends QueryEnum, UA extends UserActionEnum> {

    /**
     * Requests the model to load the initial data.
     */
    public void loadInitialQueries();

}