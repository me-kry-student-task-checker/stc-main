<template lang="pug">
	b-modal(id="studentInviteModal", hide-footer, title="Invite Students", size="xl")
		div
			input(type="search", placeholder="Filter users", v-model="searchTextFragment").form-control
			div.list
				p.litem(v-for="(student) in studentList",
					v-bind:class="{ selected: isSelected(student.email) }",
					@click="addToInviteList(student.email)") {{student.firstName}} {{student.lastName}} - {{student.email}}
			button(@click="$emit('invite-students', studentsToInvite)",
				:disabled="isDisabled").panelBtn Send invites
</template>

<script>
	// TODO close on ok, message for invite success, reload invitable students on open
	export default {
		name: 'InviteStudentsModal',
		props: {
			users: {
				type: Array,
				required: true
			}
		},
		data: function() {
			return {
				searchTextFragment: '',
				itemsToShow: [],
				studentsToInvite: []
			};
		},
		computed: {
			studentList: function() {
				if (!this.searchTextFragment) {
					return this.itemsToShow;
				} else {
					return this.itemsToShow.filter((user) => {
						let foundInName = user.name.toLowerCase().indexOf(this.searchTextFragment.toLowerCase()) > -1;
						let foundInEmail = user.email.toLowerCase().indexOf(this.searchTextFragment.toLowerCase()) > -1;
						return foundInName || foundInEmail;
					});
				}
			},
			isDisabled: function() {
				return this.studentsToInvite.length === 0;
			}
		},
		created() {
			this.itemsToShow = this.users;
		},
		methods: {
			addToInviteList(email) {
				for (let i = 0; i < this.studentsToInvite.length; i++) {
					if (this.studentsToInvite[i] === email) {
						this.studentsToInvite.splice(i, 1);
						return;
					}
				}
				this.studentsToInvite.push(email);
			},
			isSelected: function(email) {
				for (let i = 0; i < this.studentsToInvite.length; i++) {
					if (this.studentsToInvite[i] === email) {
						return true;
					}
				}
				return false;
			}
		}
	};
</script>

<style lang="scss" scoped>
	.list {
		display: flex;
		flex-direction: row;
		flex-wrap: wrap;
		justify-content: left;
	}
	.litem {
		list-style-type: none;
		margin: 5px 2px 2px 15px;
		max-width: 250px;
		overflow: hidden;
		padding: 5px;
		text-overflow: ellipsis;
		width: 100%;
		&:hover {
			cursor: pointer;
			background: $grey-font;
		}
	}
	.selected {
		color: $light;
		background: $bg;
		&:hover {
			cursor: pointer;
			background: $grey-font;
		}
	}
	.panelBtn {
		background: $secondary;
		border-radius: 5px;
		color: $light;
		font-size: 18px;
		margin-top: 10px;
		min-height: 40px;
		width: 100%;
		&:hover {
			background: #333333;
			cursor: pointer;
		}
		&:disabled {
			background: grey;
		}
	}
</style>
