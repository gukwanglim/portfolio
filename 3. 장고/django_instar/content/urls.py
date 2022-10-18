from django.urls import path
from .views import Main, UploadFeed, Profile, UploadReply, ToggleLike, ToggleBookmark, FeedControl, ReplyDelete

urlpatterns = [
    path('main', Main.as_view()),
    path('upload', UploadFeed.as_view()),
    path('reply', UploadReply.as_view()),
    path('profile', Profile.as_view()),
    path('like', ToggleLike.as_view()),
    path('bookmark', ToggleBookmark.as_view()),
    path('delete', FeedControl.as_view()),
    path('redelete', ReplyDelete.as_view())
]