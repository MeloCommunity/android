# MeloCommunity

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)

## Overview
### Description
An app that builds on the basic spotify to add comment functionality to songs. Users can login and post comments on songs, read and reply to comments of other users. It gives people a way to interact and connect over songs.

### App Evaluation
- **Category**: Music, Entertainment, Forums, Social Networking
- **Mobile**: Mobile is essential for user convinience and targets our demographic.
- **Story**: Creates a post out of a song that is the centerpiece for discussion.
- **Market**: Spotify and other music enthusiasts that want to discuss music with a community.
- **Habit**: Users are using this whenever they are introduced to a new song or are passionate about a song they heard.
- **Scope**: V1 Allow users to search and view a song post in detail that has likes and a comment section for discussion. V2 Would include verified arist accounts.

## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

* As a user I should be able to register an account
* [x] As a user I should be able to log into my account
* As a user I should be able to see a feed of posts
* As a user I should be able to search for a song or artist
* As a user I should be able to follow an artist
* As a user I should be able to like a post
* As a user I should be able to comment on a post
* As a user I should be able to log out of my account



**Optional Nice-to-have Stories**

* As a user I should be able to share a post
* As a user I should be able to save/archive a post
* As a user I should be able to private/public my profile
* As a user I should be able to follow other users
* As a user I should be able to view my own profile
* As a user I should be able to create a playlist of posts

### 2. Screen Archetypes
* Registration Screen
   * As a user I should be able to register an account
* Login Screen
   * As a user I should be able to log into my account
* Stream
   * As a user I should be able to see a feed of posts
* Search
   * As a user I should be able to search for a song or artist 
   * As a user I should be able to follow an artist
* Detail
   * As a user I should be able to like a post
   * As a user I should be able to comment on a post
* Settings
   * As a user I should be able to log out of my account

### 3. Navigation

**Tab Navigation** (Tab to Screen)

* Home Feed
* Search

**Flow Navigation** (Screen to Screen)

* Registration
   => Login
* Login
   => Home
* Home
   => Feed (Stream)
   => Search
   => Settings
* Feed (Stream)
   => Detail

## Wireframes
<img src="https://user-images.githubusercontent.com/32282010/113899803-2ae04d00-979b-11eb-8b50-0608145a7f3d.png" width=600>

### [BONUS] Digital Wireframes & Mockups
#### Digital Wireframe
<img src="https://user-images.githubusercontent.com/32282010/113899803-2ae04d00-979b-11eb-8b50-0608145a7f3d.png" width=600>

### [BONUS] Interactive Prototype

## Schema 
### Models
#### Account
 | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | userID      | String   | taken from spotify account user id |
   | image         | Image     | image of user taken from spotify |
   | liked songs       | Arraylist of Posts   | list of liked posts/songs taken from the user |
   
#### Post
 | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | songID      | String   | from spotify |
   | nameSong         | String     |  name of song |
   | nameArtist     | String  | name of artist |
   | nameAlbum      | String   | name of album |
   | image         | Image     |  image of song/album |
   | comments     |  Arraylist of Comments  | comments on this specific song |
   | streamsCount     |  Integer |  number of streams (if available on the api) |
   | createdAt     |  DateTime  |  date song was created at |

#### Artist 
 | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | artistID      | String   | artists id from spotify |
   | name         | String     | name of artist |
   | image     | Image  |  image of artist |
   | feed      | Array of Posts   | taken from spotify |
   | biography         | String     |  from spotify |
   | monthlyListener     |  Integer |  from spotify |
   
#### Comment 
 | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | songId      | String   | what song is it about |
   | commentId         | String     | comment’s unique id |
   | userId     | String  |  user who left the comment |
   | createdAt      | DateTime  | time comment was posted |
   | description         | Text     |  the actual comment  |
   
   
### Networking
#### List of network requests by screen
  - Home Feed Screen
    - (Read/GET) Query all posts from liked songs by user 
  - Profile screen
    - (Read/GET) Query logged in user object
  - Artists screen
    - (Read/GET) Query artists information
  - Detail Screen
    - (Create/POST) Create a new comment on a post/song
    - (Update/PUT) Edit an existing comment
    - (Delete) Delete an existing comment
    
#### [OPTIONAL:] Existing API Endpoints
##### Spotify API
- Base URL - [https://github.com/kaaes/spotify-web-api-android](https://github.com/kaaes/spotify-web-api-android)

   HTTP Verb | Endpoint | Description
   ----------|----------|------------
    `GET`    | /playlists | query all posts from liked songs by user 
    `GET`    | /me   | query logged in user object
    `GET`    | /artists/{id}	 |  query artists information

### Gif showing progress so far:
(https://user-images.githubusercontent.com/53497620/116325540-62398c80-a790-11eb-8873-0c04d10903bb.gif)

