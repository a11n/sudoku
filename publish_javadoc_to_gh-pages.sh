#!/bin/sh

echo "1. Generate javadoc"
./gradlew javadoc

echo "2. Checkout and clean gh-pages"
REPO=https://github.com/a11n/sudoku.git
BRANCH=gh-pages
FOLDER=$BRANCH

git clone -q $REPO --branch $BRANCH --single-branch $FOLDER
cd $FOLDER
git rm -rf . > /dev/null

echo "3. Copy javdoc and deploy"
cp -r ../build/docs/javadoc/ ./
git add .
git commit -m "Published javadoc." > /dev/null
git push -q

echo "4. Clean-up"
cd ..
rm -rf $FOLDER