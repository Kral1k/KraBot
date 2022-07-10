package com.kral1k.krabot.api;

import com.kral1k.krabot.utils.GsonHelper;

import java.net.http.HttpResponse;
import java.util.List;

public class YouTubeSearchResponse {
    public final int statusCode;
    public final Body body;

    public YouTubeSearchResponse(HttpResponse<String> stringHttpResponse) {
        this.statusCode = stringHttpResponse.statusCode();
        this.body = GsonHelper.deserialize(stringHttpResponse.body(), Body.class);
    }

    public static class Body {
        private List<Item> items;
        private PageInfo pageInfo;

        public List<Item> getItems() {
            return items;
        }

        public Item getItem(int index) {
            return items.get(index);
        }

        public boolean isItemEmpty() {
            return items.isEmpty();
        }

        public PageInfo getPageInfo() {
            return pageInfo;
        }

        public static class PageInfo {
            private int totalResults;
            private int resultsPerPage;

            public int getTotalResults() {
                return totalResults;
            }

            public int getResultsPerPage() {
                return resultsPerPage;
            }
        }

        public static class Item {

            private Id id;
            private Snippet snippet;

            public Id getId() {
                return id;
            }

            public Snippet getSnippet() {
                return snippet;
            }

            public static class Id {
                private String kind;
                private String videoId;
                private String playlistId;
                private String channelId;

                public String getKind() {
                    return kind;
                }

                public String getVideoId() {
                    return videoId;
                }

                public String getPlaylistId() {
                    return playlistId;
                }

                public String getChannelId() {
                    return channelId;
                }
            }

            public static class Snippet {
                private String channelId;
                private String title;
                private String description;
                private String channelTitle;

                public String getChannelId() {
                    return channelId;
                }

                public String getTitle() {
                    return title;
                }

                public String getDescription() {
                    return description;
                }

                public String getChannelTitle() {
                    return channelTitle;
                }
            }
        }
    }
}
