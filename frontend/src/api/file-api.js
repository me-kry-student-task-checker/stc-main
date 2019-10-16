import axios from 'axios';

export default {
	uploadFiles(files, service, tagId) {
		let formData = new FormData();
		files.forEach((file) => {
			formData.append('file', file);
		});
		formData.append('service', service);
		formData.append('tagId', tagId);
		axios.post('/api/filemanagement/uploadFiles', formData,
			{
				headers: {
					'Content-Type': 'multipart/form-data'
				}
			});
	}
};
