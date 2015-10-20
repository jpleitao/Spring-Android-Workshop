# REST Client Demo

## Basic Info ##

This is the base code to be used in the Spring Workshop, taught in the scope of the Mobile Computing Course of the Master's in Informatics Engineering of the University of Coimbra.

## Authors ##

1. **Inês Coelho**
2. **Joaquim Leitão**
3. **Matic Krizaj**

Table of Contents
=================
* [Scope of this Application](#scope-of-this-application)
* [Getting Started](#getting-started)
* [What do I need to run the demo code?](#what-do-i-need-to-run-the-demo-code)
* [Target Behaviour](#target-behaviour)
* [What do I need to implement?](#what-do-i-need-to-implement)

# Scope of this Application

In this repository we present the starting point for an application that, if all goes well, will allow users to generate memes. Along with the code, we already provide a set of *Star Wars* content (images and quotes) that can be used as a starting point.

The application must make use of the **[Spring Framework for Android](http://projects.spring.io/spring-android/)** to consume a RESTfull WebService where all the meme generation will take place.

# Getting Started

The application, as it is provided in this repository, has the following behaviour:

1. When the user stars the application it displays a starting screen with two possible choices of action: *Upload* and *Random*.

2. If the user selects the *Upload* option then he/she must provide an image and a quote for the meme. The image must be already stored in the phone/emulator, so if you want to use a custom image make sure to transfer it before running the application. Besides selecting the desired image, the user must also select a quote by typing it in the edit text box below the image view box. To upload the image to the 
 
3. If the user selects the *Random* option then all the content selection for the meme will be made automatically. Since we already provide a series of images and quotes, the user must simply click on the *Spin* button to randomly generate an image and a quote. The user is free to click on the *Spin* button as many times as he/she wants, until a desired combination *image-quote* is found. When such combination is found, the user simply has to click on the *Upload* to upload the image to the RESTfull WebService.

# What do I need to run the demo code?

1. Make sure you have internet connection
2. Install **[Android Studio](http://developer.android.com/sdk/index.html#top)** (includes the Android SDK)
2. Open this folder in Android Studio
3. If required, install the SDK build tools for the required API Level (API 8, Froyo)
4. Start your Android Emulator (The current application has been tested with the **[Genymotion Android Emulator](https://www.genymotion.com/#!/download)** but it should also work with the Android Virtual Device provided with Android Studio)


# Target Behaviour

The target behaviour for this application is the following:

1. When the user stars the application it displays a starting screen with two possible choices of action: *Upload* and *Random*.

2. If the user selects the *Upload* option then he/she must provide an image and a quote for the meme. The image must be already stored in the phone/emulator, so if you want to use a custom image make sure to transfer it before running the application. Besides selecting the desired image, the user must also select a quote by typing it in the edit text box below the image view box. To upload the image to the RESTfull WebService, the user simply needs to click on the *Upload* button. This WebService will generate the meme based on the image and quote provided, returning the generated image to the application. Once the application receives the generated meme it replaces the selected image with the processed one, allowing the user to acknowledge the completion of the task.
 
3. If the user selects the *Random* option then all the content selection for the meme will be made automatically. Since we already provide a series of images and quotes, the user must simply click on the *Spin* button to randomly generate an image and a quote. The user is free to click on the *Spin* button as many times as he/she wants, until a desired combination *image-quote* is found. When such combination is found, the user simply has to click on the *Upload* button to upload the image to the RESTfull WebService. The behaviour of the WebService in this situation is the same as what was described in the previous point of this listing. Once the task is completed the user can save the image to the phone's gallery, by pressing the button *Save*.

# What do I need to implement?

During the Workshop we will implement the interaction between the Android Application and the RESTfull WebService using the *Spring for Android* framework.

But don't worry! You will not be alone in this task! We will code with you, explaining each step in detail.

We will start by creating a version of the application capable of sending, receiving and displaying the desired images. After that, if we still have time left in the workshop we will implement the *save image* functionality.
