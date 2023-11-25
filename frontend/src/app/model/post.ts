import { Tag } from "./tag";
import { User } from "./user";

export class Post {
	id: number;
	content: string;
	postPhoto: string;
	img:any
	postPhotoByte:any
	likeCount: number;
	commentCount: number;
	shareCount: number;
	dateCreated: string;
	dateLastModified: string;
	isTypeShare: boolean;
	author: User;
	sharedPost: Post;
	postTags: Tag[]
}
