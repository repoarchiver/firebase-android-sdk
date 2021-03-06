// Copyright 2018 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.firebase.firestore.model;

import com.google.firebase.firestore.model.value.FieldValue;
import com.google.firebase.firestore.model.value.ObjectValue;
import java.util.Comparator;
import javax.annotation.Nullable;

/**
 * Represents a document in Firestore with a key, version, data and whether the data has local
 * mutations applied to it.
 */
public class Document extends MaybeDocument {

  private static final Comparator<Document> KEY_COMPARATOR =
      new Comparator<Document>() {
        @Override
        public int compare(Document left, Document right) {
          return left.getKey().compareTo(right.getKey());
        }
      };

  /** A document comparator that returns document by key and key only. */
  public static Comparator<Document> keyComparator() {
    return KEY_COMPARATOR;
  }

  private final ObjectValue data;

  private final boolean hasLocalMutations;

  public Document(
      DocumentKey key, SnapshotVersion version, ObjectValue data, boolean hasLocalMutations) {
    super(key, version);
    this.data = data;
    this.hasLocalMutations = hasLocalMutations;
  }

  public ObjectValue getData() {
    return data;
  }

  public @Nullable FieldValue getField(FieldPath path) {
    return data.get(path);
  }

  public @Nullable Object getFieldValue(FieldPath path) {
    FieldValue value = getField(path);
    return (value == null) ? null : value.value();
  }

  public boolean hasLocalMutations() {
    return hasLocalMutations;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Document document = (Document) o;

    return getVersion().equals(document.getVersion())
        && getKey().equals(document.getKey())
        && hasLocalMutations == document.hasLocalMutations
        && data.equals(document.data);
  }

  @Override
  public int hashCode() {
    int result = getKey().hashCode();
    result = 31 * result + data.hashCode();
    result = 31 * result + getVersion().hashCode();
    result = 31 * result + (hasLocalMutations ? 1 : 0);
    return result;
  }

  @Override
  public String toString() {
    return "Document{"
        + "key="
        + getKey()
        + ", data="
        + data
        + ", version="
        + getVersion()
        + ", hasLocalMutations="
        + hasLocalMutations
        + '}';
  }
}
