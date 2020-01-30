import axios from 'axios';

export default {
	createFileEntry(files, service, tagId) {
		let formData = new FormData();
		files.forEach((file) => {
			formData.append('file', file);
		});
		formData.append('service', service);
		formData.append('tagId', tagId);
		return axios.post('/api/filemanagement/uploadFiles', formData,
			{
				headers: {
					'Content-Type': 'multipart/form-data'
				}
			});
	}
};
