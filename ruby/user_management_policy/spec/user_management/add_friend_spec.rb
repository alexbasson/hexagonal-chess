require 'user_management/user_id'
require 'user_management/friendship'
require 'user_management/friendship_repository'
require 'user_management/add_friend'

RSpec.describe UserManagement::AddFriend do
  subject(:add_friend) { described_class.new(friendship_repository) }

  let(:friendship_repository) { instance_double(UserManagement::FriendshipRepository) }
  let(:owner_id)              { UserManagement::UserId.new(value: 'u1') }
  let(:friend_id)             { UserManagement::UserId.new(value: 'u2') }

  before { allow(friendship_repository).to receive(:save) }

  it 'saves the friendship' do
    add_friend.call(owner_id, friend_id)

    expect(friendship_repository).to have_received(:save).with(
      UserManagement::Friendship.new(owner_id: owner_id, friend_id: friend_id)
    )
  end
end
